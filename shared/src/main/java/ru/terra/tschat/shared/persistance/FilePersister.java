package ru.terra.tschat.shared.persistance;

import ru.terra.tschat.shared.config.Config;
import ru.terra.tschat.shared.config.ConfigConstants;

import java.io.File;

/**
 * Date: 25.04.14
 * Time: 15:05
 */
public abstract class FilePersister {
    protected String dir = Config.getConfig().getValue(ConfigConstants.PERSISTANCE_DIR, ConfigConstants.PERSISTANCE_DIR_DEFAULT);

    public FilePersister() {
        File storageDir = new File(dir);
        if (!storageDir.exists())
            storageDir.mkdirs();
    }
}
