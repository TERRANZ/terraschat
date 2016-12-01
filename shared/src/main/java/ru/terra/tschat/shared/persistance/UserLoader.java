package ru.terra.tschat.shared.persistance;

import ru.terra.tschat.shared.entity.UserInfo;

import java.util.List;

/**
 * Date: 25.04.14
 * Time: 14:34
 */
public interface UserLoader {
    UserInfo loadUser(Long uid);

    List<UserInfo> loadUsers();

    Long findUser(String login, String pass);
}
