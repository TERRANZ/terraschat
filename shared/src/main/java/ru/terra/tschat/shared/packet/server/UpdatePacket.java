package ru.terra.tschat.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * Date: 13.01.14
 * Time: 13:51
 */
@Packet(opCode = OpCodes.Server.SMSG_UPDATE)
public class UpdatePacket extends AbstractPacket {
    private int field = 0;
    private String value = "";

    public UpdatePacket() {
    }

    public UpdatePacket(int field, String value) {
        this.field = field;
        this.value = value;
    }

    public int getField() {
        return field;
    }

    public void setField(int field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void onRead(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) {
        field = buffer.readInt();
        checkpointHandler.onCheckpoint();
        value = readString(buffer, checkpointHandler);
        checkpointHandler.onCheckpoint();
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        buffer.writeInt(field);
        writeString(buffer, value);
    }
}
