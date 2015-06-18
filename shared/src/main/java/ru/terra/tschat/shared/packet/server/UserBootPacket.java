package ru.terra.tschat.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.Server.SMSG_USER_BOOT)
public class UserBootPacket extends AbstractPacket {

    private UserInfo userInfo = new UserInfo();

    @Override
    public void onRead(ChannelBuffer buffer) {
        userInfo.readUserInfo(buffer);
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        userInfo.writeUserInfo(buffer);
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo playerInfo) {
        this.userInfo = playerInfo;
    }
}
