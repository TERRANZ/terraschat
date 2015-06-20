package ru.terra.tschat.shared.packet.interserver;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

import java.util.Date;

/**
 * Date: 20.06.15
 * Time: 13:29
 */
@Packet(opCode = OpCodes.InterServer.ISMSG_PING)
public class PingPacket extends AbstractPacket {
    private long ts = 0l;

    @Override
    public void onRead(ChannelBuffer buffer) {
        ts = buffer.readLong();
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        buffer.writeLong(new Date().getTime());
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }
}
