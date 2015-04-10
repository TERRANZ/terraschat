package ru.terra.tschat.shared.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.context.SharedContext;

import java.io.IOException;
import java.nio.charset.Charset;

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
        byte[] buf = new byte[length];
        buffer.readBytes(buf);
        SharedContext.getInstance().getLogger().debug("AbstractPacket", "readed: " + bytesToHex(buf));
        String text = "";
        try {
//            text = SimpleCrypto.decrypt("this is a key", buf);
            text = new String(buf, Charset.forName("UTF-8"));
        } catch (Exception e) {
            SharedContext.getInstance().getLogger().error("AbstractPacket", "Unable to decode string", e);
        }
        return text;
    }

    public static void writeString(ChannelBuffer buffer, String text) {
        if (text == null || text.length() == 0)
            return;
        try {
//            byte[] encryptedData = SimpleCrypto.encrypt("this is a key", text);
            byte[] encryptedData = text.getBytes(Charset.forName("UTF-8"));
            buffer.writeShort(encryptedData.length);
            SharedContext.getInstance().getLogger().debug("AbstractPacket", "written: " + bytesToHex(encryptedData));
            buffer.writeBytes(encryptedData);
        } catch (Exception e) {
            SharedContext.getInstance().getLogger().error("AbstractPacket", "Unable to encode and send string " + text, e);
        }

    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
