package ru.terra.tschat.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import ru.terra.tschat.client.service.ChatService;

/**
 * Date: 24.06.14
 * Time: 18:08
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        startService(new Intent(this, ChatService.class));
    }
}
