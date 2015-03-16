package ru.terra.tschat.server.frontend.server;

import org.jboss.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * User: Vadim Korostelev
 * Date: 02.09.13
 * Time: 11:39
 */
public class TempUsersHolder {
    private static TempUsersHolder instance = new TempUsersHolder();
    private Map<Long, Channel> channels = new HashMap<>();

    private TempUsersHolder() {
    }

    public static TempUsersHolder getInstance() {
        return instance;
    }

    public Channel getTempChannel(Long id) {
        synchronized (channels) {
            return channels.get(id);
        }
    }

    public void addTempChannel(Long id, Channel channel) {
        synchronized (channels) {
            channels.put(id, channel);
        }
    }

    public long getUnusedId() {
        return UUID.randomUUID().getLeastSignificantBits();
    }

    public void deleteTempChannel(Long id) {
        {
            channels.remove(id);
        }
    }
}
