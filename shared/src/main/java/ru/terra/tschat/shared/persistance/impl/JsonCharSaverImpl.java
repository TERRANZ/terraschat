package ru.terra.tschat.shared.persistance.impl;

import flexjson.JSONSerializer;
import ru.terra.tschat.shared.config.Config;
import ru.terra.tschat.shared.config.ConfigConstants;
import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.persistance.UserLoader;
import ru.terra.tschat.shared.persistance.CharSaver;
import ru.terra.tschat.shared.persistance.FilePersister;

import java.io.*;
import java.util.List;

/**
 * Date: 25.04.14
 * Time: 14:46
 */
public class JsonCharSaverImpl extends FilePersister implements CharSaver {
    private JSONSerializer jsonSerializer = new JSONSerializer();
    private String fileName = Config.getConfig().getValue(ConfigConstants.CHARACTERS_FILE, ConfigConstants.CHARACTERS_FILE_DEFAULT);

    @Override
    public void save(UserInfo playerInfo) {
        UserLoader charLoader = new JsonCharLoaderImpl();
        List<UserInfo> playerInfos = charLoader.loadUsers();
        UserInfo playerInfoToRemove = null;
        for (UserInfo pi : playerInfos)
            if (pi.getUID().equals(playerInfo))
                playerInfoToRemove = pi;
        if (playerInfoToRemove != null)
            playerInfos.remove(playerInfoToRemove);

        playerInfos.add(playerInfo);
        save(playerInfos);
    }

    @Override
    public void save(List<UserInfo> playerInfos) {
        try {
            Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + fileName), "UTF-8"));
            jsonSerializer.serialize(playerInfos, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
