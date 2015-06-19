package ru.terra.tschat.interserver.network.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import ru.terra.tschat.shared.context.SharedContext;
import ru.terra.tschat.shared.packet.AbstractPacket;

public class PacketFrameEncoder extends OneToOneEncoder {
    @Override
    protected Object encode(ChannelHandlerContext channelhandlercontext, Channel channel, Object obj) throws Exception {
        if (!(obj instanceof AbstractPacket))
            return obj; // Если это не пакет, то просто пропускаем его дальше
        AbstractPacket p = (AbstractPacket) obj;

        ChannelBuffer packetBuffer = ChannelBuffers.dynamicBuffer();
        AbstractPacket.write(p, packetBuffer);
        ChannelBuffer outBuffer = ChannelBuffers.directBuffer(Integer.BYTES + Integer.BYTES + packetBuffer.readableBytes());
        outBuffer.writeInt(p.getOpCode());
        outBuffer.writeInt(packetBuffer.readableBytes());//size
//        SharedContext.getInstance().getLogger().debug("PacketFrameEncoder", "Writing opcode: " + p.getOpCode() + " len: " + packetBuffer.readableBytes());
        outBuffer.writeBytes(packetBuffer);//content
        return outBuffer;
    }
}