package ru.terra.tschat.interserver.network.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;
import ru.terra.tschat.shared.context.SharedContext;
import ru.terra.tschat.shared.packet.AbstractPacket;

public class PacketFrameDecoder extends ReplayingDecoder<DecoderState> {

    private int length;
    private int opcode;


    public PacketFrameDecoder() {
        super(DecoderState.HEADER);
    }

    @Override
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        ctx.sendUpstream(e);
    }

    @Override
    protected Object decode(ChannelHandlerContext arg0, Channel arg1, ChannelBuffer buffer, DecoderState e) throws Exception {
        switch (getState()) {
            case HEADER:
                opcode = buffer.readInt();
                length = buffer.readInt();
                checkpoint(DecoderState.CONTENT);
                break;
            case CONTENT:
                SharedContext.getInstance().getLogger().debug("PacketFrameDecoder", "Reading opode: " + opcode + " len: " + length);
                AbstractPacket ret = AbstractPacket.read(opcode, buffer.readBytes(length));
                checkpoint(DecoderState.HEADER);
                return ret;
        }
        return null;
    }
}