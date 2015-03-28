package ru.terra.tschat.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes.InterServer;
import ru.terra.tschat.shared.packet.AbstractPacket;

@Packet(opCode = InterServer.ISMSG_REG)
public class RegisterPacket extends AbstractPacket {

    private int startRange = 0;
    private int endRange = 0;

    @Override
    public void onRead(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) {
        startRange = buffer.readInt();
        checkpointHandler.onCheckpoint();
        endRange = buffer.readInt();
        checkpointHandler.onCheckpoint();
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        buffer.writeInt(startRange);
        buffer.writeInt(endRange);
    }

    public int getStartRange() {
        return startRange;
    }

    public void setStartRange(int startRange) {
        this.startRange = startRange;
    }

    public int getEndRange() {
        return endRange;
    }

    public void setEndRange(int endRange) {
        this.endRange = endRange;
    }

}
