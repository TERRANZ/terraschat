package ru.terra.tschat.interserver.network.netty;

import org.jboss.netty.channel.Channel;
import ru.terra.tschat.interserver.network.NetworkManager;
import ru.terra.tschat.shared.context.SharedContext;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.interserver.PingPacket;

public abstract class InterserverWorker {

    private InterserverHandler clientHandler;
    private Channel channel;
    protected NetworkManager networkManager = NetworkManager.getInstance();

    public InterserverWorker(InterserverHandler clientHandler, Channel channel) {
        this.clientHandler = clientHandler;
        this.channel = channel;
        new Thread(new PingRunnable()).start();
    }

    public InterserverWorker() {
        new Thread(new PingRunnable()).start();
    }

    public InterserverHandler getClientHandler() {
        return clientHandler;
    }

    public void setClientHandler(InterserverHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public abstract void disconnectedFromChannel();

    public abstract void acceptPacket(AbstractPacket packet);

    public class PingRunnable implements Runnable {
        @Override
        public void run() {
            while (true)
                try {
                    Thread.sleep(10000);
                    networkManager.sendPacket(new PingPacket());
                } catch (Exception e) {
                    SharedContext.getInstance().getLogger().error("InterserverWorker", "Error while sending ping", e);
                }
        }
    }
}
