package ru.terra.tschat.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.Server.SMSG_CHAR_BOOT)
public class UserBootPacket extends AbstractPacket {

    private UserInfo playerInfo = new UserInfo();

    @Override
    public void get(ChannelBuffer buffer) {
        playerInfo.readUserInfo(buffer);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        playerInfo.writeUserInfo(buffer);
    }

    public UserInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setUserInfo(UserInfo playerInfo) {
        this.playerInfo = playerInfo;
    }
}
