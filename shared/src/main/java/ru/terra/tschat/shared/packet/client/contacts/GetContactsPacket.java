package ru.terra.tschat.shared.packet.client.contacts;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * Date: 22.03.15
 * Time: 19:48
 */
@Packet(opCode = OpCodes.Client.User.CMSG_GET_CONTACTS)
public class GetContactsPacket extends AbstractPacket {
    @Override
    public void onRead(ChannelBuffer buffer) {

    }

    @Override
    public void onSend(ChannelBuffer buffer) {

    }
}
