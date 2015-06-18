package ru.terra.tschat.shared.packet.client.contacts;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * Date: 18.06.15
 * Time: 14:17
 */
@Packet(opCode = OpCodes.Client.User.CMSG_GET_CONTACT_INFO)
public class GetContactInfoPacket extends AbstractPacket {
    public GetContactInfoPacket() {
    }

    public GetContactInfoPacket(long uid) {
        this.uid = uid;
    }

    public long uid;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    @Override
    public void onRead(ChannelBuffer buffer) {
        uid = buffer.readLong();
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        buffer.writeLong(uid);
    }
}
