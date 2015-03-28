package ru.terra.tschat.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * User: Vadim Korostelev
 * Date: 17.09.13
 * Time: 17:07
 */
@Packet(opCode = OpCodes.InterServer.ISMSG_UNREG_USER)
public class UnregCharPacket extends AbstractPacket {

    @Override
    public void onRead(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) {

    }

    @Override
    public void onSend(ChannelBuffer buffer) {

    }
}
