package ru.terra.tschat.client.framework;

import ru.terra.tschat.shared.entity.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: terranz
 * Date: 22.09.13
 * Time: 0:47
 */
public class ClientManager {
    private static ClientManager instance = new ClientManager();
    private UserInfo userInfo;
    private List<UserInfo> contacts = new ArrayList<>();
    private ClientManagerNotifier notifier;

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

    public List<UserInfo> getContacts() {
        return contacts;
    }

    public void setContacts(List<UserInfo> contacts) {
        this.contacts = contacts;
    }

    public ClientManagerNotifier getNotifier() {
        return notifier;
    }

    public void setNotifier(ClientManagerNotifier notifier) {
        this.notifier = notifier;
    }

    public void event(ClientManagerNotifier.ClientManagerEvent event) {
        if (notifier != null)
            notifier.onEvent(event);
    }
}
