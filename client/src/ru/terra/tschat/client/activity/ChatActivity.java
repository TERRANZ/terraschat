package ru.terra.tschat.client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import ru.terra.tschat.client.R;
import ru.terra.tschat.client.service.ChatService;

/**
 * Date: 18.03.15
 * Time: 17:39
 */
public class ChatActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_chat);
        startService(new Intent(this, ChatService.class));
    }
}