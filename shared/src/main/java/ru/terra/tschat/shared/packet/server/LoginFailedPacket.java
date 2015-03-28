package ru.terra.tschat.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * Date: 25.04.14
 * Time: 15:39
 */
@Packet(opCode = OpCodes.Server.Login.SMSG_LOGIN_FAILED)
public class LoginFailedPacket extends AbstractPacket {
    private String reason = "";

    public LoginFailedPacket() {
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void onRead(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) {
        reason = readString(buffer, checkpointHandler);
        checkpointHandler.onCheckpoint();
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        writeString(buffer, reason);
    }
}
