package ru.terra.tschat.client;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import ru.terra.tschat.client.activity.ChatActivity;
import ru.terra.tschat.client.activity.LoginActivity;
import ru.terra.tschat.client.activity.RegActivity;
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
//        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ChatService.CHAT_SERVICE_RECEIVER).putExtra(ChatService.PARAM_DO, ChatService.DO_CONNECT));
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getBoolean(getString(R.string.loggedIn), false)) {

            LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ChatService.CHAT_SERVICE_RECEIVER).putExtra(ChatService.PARAM_DO, ChatService.DO_LOGIN));
            startActivity(new Intent(this, ChatActivity.class));
        }
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void reg(View view) {
        startActivity(new Intent(this, RegActivity.class));
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }
    }
}
