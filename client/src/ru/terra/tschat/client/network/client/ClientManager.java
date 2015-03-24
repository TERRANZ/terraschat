package ru.terra.tschat.client.network.client;

import ru.terra.tschat.shared.entity.UserInfo;

/**
 * Created with IntelliJ IDEA.
 * User: terranz
 * Date: 22.09.13
 * Time: 0:47
 * To change this template use File | Settings | File Templates.
 */
public class ClientManager {
    private static ClientManager instance = new ClientManager();
    private UserInfo userInfo;

    private ClientManager() {
    }

    public static ClientManager getInstance() {
        return instance;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
