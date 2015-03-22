package ru.terra.tschat.shared.packet.client.login;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.Client.Login.CMSG_LOGOUT)
public class LogoutPacket extends AbstractPacket {
    @Override
    public void get(ChannelBuffer buffer) {
    }

    @Override
    public void send(ChannelBuffer buffer) {
    }
}
