package ru.terra.tschat.shared.packet.server.user;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * Date: 22.03.15
 * Time: 20:11
 */
@Packet(opCode = OpCodes.Server.User.SMSG_CONTACT_INFO)
public class UserInfoPacket extends AbstractPacket {
    private UserInfo userInfo;

    public UserInfoPacket() {
    }

    public UserInfoPacket(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public void onRead(ChannelBuffer buffer) {
        userInfo = new UserInfo();
        userInfo.setName(readString(buffer));
        userInfo.setUID(buffer.readLong());
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        writeString(buffer, userInfo.getName());
        buffer.writeLong(userInfo.getUID());
    }
}
