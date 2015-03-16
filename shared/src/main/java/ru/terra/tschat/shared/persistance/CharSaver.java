package ru.terra.tschat.shared.persistance;

import ru.terra.tschat.shared.entity.UserInfo;

import java.util.List;

/**
 * Date: 25.04.14
 * Time: 14:46
 */
public interface CharSaver {
    public void save(UserInfo playerInfo);

    public void save(List<UserInfo> playerInfos);
}
