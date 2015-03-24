package ru.terra.tschat.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

@Packet(opCode = OpCodes.InterServer.ISMSG_USER_REG)
public class UserRegPacket extends AbstractPacket {
    private Long oldId = 0l;

    @Override
    public void onRead(ChannelBuffer buffer) {
        oldId = buffer.readLong();
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        buffer.writeLong(oldId);
    }

    public Long getOldId() {
        return oldId;
    }

    public void setOldId(Long oldId) {
        this.oldId = oldId;
    }
}
