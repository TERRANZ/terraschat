package ru.terra.tschat.server.frontend.network.netty;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import ru.terra.tschat.interserver.network.netty.PacketFrameDecoder;
import ru.terra.tschat.interserver.network.netty.PacketFrameEncoder;

public class ServerPipelineFactory implements ChannelPipelineFactory {

    private Class<? extends ServerWorker> serverWorker;

    public ServerPipelineFactory(Class<? extends ServerWorker> serverWorker) {
        this.serverWorker = serverWorker;
    }

    @Override
    public ChannelPipeline getPipeline() throws Exception {
        PacketFrameDecoder decoder = new PacketFrameDecoder();
        PacketFrameEncoder encoder = new PacketFrameEncoder();
        return Channels.pipeline(decoder, encoder, new ServerHandler(decoder, encoder, serverWorker));
    }

}
