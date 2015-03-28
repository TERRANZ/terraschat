package ru.terra.tschat.server.frontend.server;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * User: Vadim Korostelev
 * Date: 02.09.13
 * Time: 11:03
 */
public class UsersHolder {
    private static UsersHolder instance = new UsersHolder();
    private HashMap<Long, Channel> users = new HashMap<>();
    private Logger logger = Logger.getLogger(this.getClass());

    protected UsersHolder() {
    }

    public static UsersHolder getInstance() {
        return instance;
    }

    public synchronized Channel getUserChannel(Long userUID) {
        return users.get(userUID);
    }

    public synchronized void addUserChannel(Long userUID, Channel channel) {
        if (channel == null)
            logger.error("Channel is null");
        if (userUID == null)
            logger.error("userUID is null");
        logger.info("Adding channel " + channel.getId() + " for user " + userUID);
        users.put(userUID, channel);
    }

    public synchronized List<Long> getChannels() {
        return new ArrayList<>(users.keySet());
    }

    public synchronized int size() {
        return users.size();
    }

    public synchronized Long deleteUserChannel(Channel channel) {
        Long removedUser = null;
        for (Long guid : users.keySet()) {
            if (users.get(guid).equals(channel))
                removedUser = guid;
        }
        if (removedUser != null) {
            logger.info("Removing user " + removedUser);
            logger.info("Removed channel " + users.remove(removedUser).getId());
            return removedUser;
        }
        return null;
    }
}
