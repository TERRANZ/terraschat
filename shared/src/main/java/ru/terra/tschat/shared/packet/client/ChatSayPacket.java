package ru.terra.tschat.shared.packet.client;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * Date: 14.01.14
 * Time: 13:45
 */
@Packet(opCode = OpCodes.Client.Chat.SAY)
public class ChatSayPacket extends AbstractPacket {
    private String message = "";
    private long to = 0L;

    public ChatSayPacket() {
    }


    public ChatSayPacket(String message, long to) {
        this.message = message;
        this.to = to;
    }

    @Override
    public void get(ChannelBuffer buffer) {
        message = readString(buffer);
        to = buffer.readLong();
    }

    @Override
    public void send(ChannelBuffer buffer) {
        writeString(buffer, message);
        buffer.writeLong(to);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }
}
