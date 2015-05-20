package ru.terra.tschat.shared.packet.server.chat;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * Date: 24.03.15
 * Time: 12:18
 */
@Packet(opCode = OpCodes.Server.Chat.SMSG_CHAT_MESSAGE)
public class ChatMessagePacket extends AbstractPacket {
    private String msg = "";
    private Long from = 0l, to = 0l;

    public Long getTo() {
        return to;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public Long getFrom() {
        return from;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public void onRead(ChannelBuffer buffer) {
        from = buffer.readLong();
        to = buffer.readLong();
        msg = readString(buffer);
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        buffer.writeLong(from);
        buffer.writeLong(to);
        writeString(buffer, msg);
    }
}
