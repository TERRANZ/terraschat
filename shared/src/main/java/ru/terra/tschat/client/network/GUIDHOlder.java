package ru.terra.tschat.client.network;


import ru.terra.tschat.shared.context.SharedContext;

/**
 * User: Vadim Korostelev
 * Date: 02.09.13
 * Time: 12:29
 */
public class GUIDHOlder {
    private static GUIDHOlder instance = new GUIDHOlder();
    private long guid = 0;

    private GUIDHOlder() {
    }

    public static GUIDHOlder getInstance() {
        return instance;
    }

    public long getGuid() {
        return guid;
    }

    public void setGuid(long guid) {
        SharedContext.getInstance().getLogger().info("GUIDHolder", String.valueOf(guid));
        this.guid = guid;
    }
}
