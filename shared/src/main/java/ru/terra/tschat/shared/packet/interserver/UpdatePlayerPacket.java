package ru.terra.tschat.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * Date: 13.01.14
 * Time: 16:38
 */
@Packet(opCode = OpCodes.InterServer.ISMSG_UPDATE_USER)
public class UpdatePlayerPacket extends AbstractPacket {
    private UserInfo playerInfo;

    public UpdatePlayerPacket() {
    }

    public UpdatePlayerPacket(UserInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public UserInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(UserInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    @Override
    public void onRead(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) {
        playerInfo.readUserInfo(buffer, checkpointHandler);
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        playerInfo.writeUserInfo(buffer);
    }
}
