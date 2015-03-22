package ru.terra.tschat.server.userserver;

import ru.terra.tschat.shared.entity.UserInfo;

/**
 * Date: 20.03.15
 * Time: 22:35
 */
public class UsersHandler {
    private static UsersHandler instance = new UsersHandler();

    public static UsersHandler getInstance() {
        return instance;
    }

    private UsersHandler() {
    }

    public void getRoster(UserInfo userInfo) {
    }
}
