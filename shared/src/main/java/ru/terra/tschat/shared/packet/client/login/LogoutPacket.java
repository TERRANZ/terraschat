package ru.terra.tschat.shared.packet.client.login;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.Client.Login.CMSG_LOGOUT)
public class LogoutPacket extends AbstractPacket {
    @Override
    public void onRead(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) {
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
    }
}
