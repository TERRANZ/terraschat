package ru.terra.tschat.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes.InterServer;
import ru.terra.tschat.shared.packet.AbstractPacket;

@Packet(opCode = InterServer.ISMSG_HELLO)
public class HelloPacket extends AbstractPacket {

    private String hello;

    public String getHello() {
        return hello;
    }

    public void setHello(String hello) {
        this.hello = hello;
    }

    @Override
    public void onRead(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) {
        hello = readString(buffer, checkpointHandler);
        checkpointHandler.onCheckpoint();
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        writeString(buffer, hello);
    }

}
