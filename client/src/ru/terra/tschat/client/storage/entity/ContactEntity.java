package ru.terra.tschat.client.storage.entity;

import android.net.Uri;
import android.provider.BaseColumns;
import ru.terra.tschat.client.util.Constants;

public interface ContactEntity extends BaseColumns {
    String CONTENT_DIRECTORY = "contact";
    Uri CONTENT_URI = Uri.parse("content://" + Constants.AUTHORITY + "/" + CONTENT_DIRECTORY);
    String CONTENT_TYPE = "entity.cursor.dir/" + CONTENT_DIRECTORY;
    String CONTENT_ITEM_TYPE = "entity.cursor.item/" + CONTENT_DIRECTORY;

    String UID = "c_uid";
    String NAME = "c_name";
}
