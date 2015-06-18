package ru.terra.tschat.server.userserver;

import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.persistance.UserLoader;
import ru.terra.tschat.shared.persistance.impl.JsonUserLoaderImpl;

import java.util.List;

/**
 * Date: 20.03.15
 * Time: 22:35
 */
public class UsersHandler {
    private static UsersHandler instance = new UsersHandler();
    private UserLoader charLoader = new JsonUserLoaderImpl();

    public static UsersHandler getInstance() {
        return instance;
    }

    private UsersHandler() {
    }

    public List<UserInfo> getContacts(UserInfo userInfo) {
        return charLoader.loadUsers();
    }

    public void addContact(UserInfo userInfo, String contact) {
    }
}
