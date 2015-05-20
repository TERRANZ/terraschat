package ru.terra.tschat.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.InterServer.ISMSG_BOOT_USER)
public class BootUserPacket extends AbstractPacket {
    @Override
    public void onRead(ChannelBuffer buffer) {

    }

    @Override
    public void onSend(ChannelBuffer buffer) {

    }
}
