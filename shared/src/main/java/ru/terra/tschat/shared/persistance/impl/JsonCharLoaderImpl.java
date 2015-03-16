package ru.terra.tschat.shared.persistance.impl;

import flexjson.JSONDeserializer;
import ru.terra.tschat.shared.config.Config;
import ru.terra.tschat.shared.config.ConfigConstants;
import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.persistance.UserLoader;
import ru.terra.tschat.shared.persistance.FilePersister;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Date: 25.04.14
 * Time: 14:39
 */
public class JsonCharLoaderImpl extends FilePersister implements UserLoader {

    private JSONDeserializer<List<UserInfo>> deserializer = new JSONDeserializer<>();
    private String fileName = Config.getConfig().getValue(ConfigConstants.CHARACTERS_FILE, ConfigConstants.CHARACTERS_FILE_DEFAULT);

    @Override
    public UserInfo loadUser(Long uid) {
        for (UserInfo playerInfo : loadUsers())
            if (playerInfo.getUID().equals(uid))
                return playerInfo;
        UserInfo playerInfo = new UserInfo();
        playerInfo.setUID(uid);
        playerInfo.setName("My Cool player " + playerInfo.getUID());
        return playerInfo;
    }

    @Override
    public List<UserInfo> loadUsers() {
        try {
            return deserializer.use(null, ArrayList.class).use("values", UserInfo.class).deserialize(new FileReader(dir + fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Long findUser(String login, String pass) {
        //TODO:
        Long uid = UUID.randomUUID().getLeastSignificantBits();
        return uid;
    }
}
