package ru.terra.tschat.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * User: Vadim Korostelev
 * Date: 30.08.13
 * Time: 16:20
 */
@Packet(opCode = OpCodes.Server.SMSG_OK)
public class OkPacket extends AbstractPacket {
    public OkPacket() {
    }

    @Override
    public void onRead(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
