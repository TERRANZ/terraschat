package ru.terra.tschat.client.chat;

import ru.terra.tschat.shared.entity.UserInfo;

/**
 * Created with IntelliJ IDEA.
 * User: terranz
 * Date: 22.09.13
 * Time: 0:47
 * To change this template use File | Settings | File Templates.
 */
public class ChatManager {
    private static ChatManager instance = new ChatManager();
    private UserInfo userInfo;

    private ChatManager() {
    }

    public static ChatManager getInstance() {
        return instance;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
