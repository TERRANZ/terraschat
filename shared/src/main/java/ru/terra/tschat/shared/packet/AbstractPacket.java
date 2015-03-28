package ru.terra.tschat.shared.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.context.SharedContext;

import java.io.IOException;

public abstract class AbstractPacket {
    private Integer opCode = 0;
    private Long sender = 0L;

    public AbstractPacket() {
        opCode = getClass().getAnnotation(Packet.class).opCode();
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Integer getOpCode() {
        return opCode;
    }

    public void setOpCode(Integer id) {
        this.opCode = id;
    }

    public static AbstractPacket read(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) throws IOException {
        Integer opcode = buffer.readUnsignedShort();
        checkpointHandler.onCheckpoint();
        Long sguid = buffer.readLong();
        checkpointHandler.onCheckpoint();
        AbstractPacket packet = PacketFactory.getInstance().getPacket(opcode, sguid);
        if (packet != null)
            packet.onRead(buffer, checkpointHandler);
        else
            SharedContext.getInstance().getLogger().error("AbstractPacket", "Unable to find packet " + opcode + " with sender guid: " + sguid);
        return packet;
    }

    public static AbstractPacket write(AbstractPacket packet, ChannelBuffer buffer) {
        buffer.writeChar(packet.getOpCode());
        buffer.writeLong(packet.getSender());
        packet.onSend(buffer);
        return packet;
    }

    // Функции, которые должен реализовать каждый класс пакета

    /**
     * Вызывается при приёме пакета, в этом методе производим вычитывание из буфера данных
     */
    public abstract void onRead(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler);

    /**
     * Вызывается при отсылке пакета, в этом методе производим запись в буфер данных
     */
    public abstract void onSend(ChannelBuffer buffer);

    public static String readString(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) {
        int length = buffer.readShort();
        checkpointHandler.onCheckpoint();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; ++i)
            builder.append(buffer.readChar());
        return builder.toString();
    }

    public static void writeString(ChannelBuffer buffer, String text) {
        if (text == null || text.length() == 0)
            return;
        buffer.writeShort(text.length());
        for (int i = 0; i < text.length(); ++i) {
            buffer.writeChar(text.charAt(i));
        }
    }
}
