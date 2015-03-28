package ru.terra.tschat.shared.packet.server.user;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * Date: 22.03.15
 * Time: 19:59
 */
@Packet(opCode = OpCodes.Server.User.SMSG_USER_CONTACTS)
public class UserContactsPacket extends AbstractPacket {
    private UserInfo info;

    public UserInfo getInfo() {
        return info;
    }

    public void setInfo(UserInfo info) {
        this.info = info;
    }

    @Override
    public void onRead(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) {
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        buffer.writeInt(info.getContacts().size());
        for (Long c : info.getContacts())
            buffer.writeLong(c);
    }
}
