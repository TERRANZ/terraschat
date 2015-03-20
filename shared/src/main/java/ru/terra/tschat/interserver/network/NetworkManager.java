package ru.terra.tschat.interserver.network;

import org.jboss.netty.channel.Channel;
import ru.terra.tschat.interserver.network.netty.InterserverWorker;
import ru.terra.tschat.shared.packet.AbstractPacket;

public class NetworkManager {
    private static NetworkManager instance = new NetworkManager();

    private NetworkManager() {
    }

    public static NetworkManager getInstance() {
        return instance;
    }

    public void start(Class<? extends InterserverWorker> workerClass, String host, Integer port) {
        Thread t = new Thread(new NetworkThread(port, host, workerClass));
        t.start();
    }

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void sendPacket(AbstractPacket p) {
        getChannel().write(p);
    }

}
