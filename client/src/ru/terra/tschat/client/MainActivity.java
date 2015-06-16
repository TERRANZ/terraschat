package ru.terra.tschat.client;

import android.app.Activity;
import android.app.ProgressDialog;
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
import ru.terra.tschat.client.framework.ClientStateHolder;
import ru.terra.tschat.client.service.ChatService;

/**
 * Date: 24.06.14
 * Time: 18:08
 */
public class MainActivity extends Activity {

    private LocalBroadcastManager lbm;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_main);
        startService(new Intent(this, ChatService.class));
        lbm = LocalBroadcastManager.getInstance(this);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(ChatService.CHAT_SERVICE_RECEIVER).putExtra(ChatService.PARAM_DO, ChatService.DO_CONNECT));
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        new LoginAsyncTask().execute();
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void reg(View view) {
        startActivity(new Intent(this, RegActivity.class));
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dlg;

        @Override
        protected void onPreExecute() {
            dlg = ProgressDialog.show(MainActivity.this, "Вход", "Выполняется вход", true);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            try {
                dlg.dismiss();
            } catch (Exception e) {
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (prefs.getBoolean(getString(R.string.loggedIn), false)) {
                lbm.sendBroadcast(new Intent(ChatService.CHAT_SERVICE_RECEIVER).putExtra(ChatService.PARAM_DO, ChatService.DO_LOGIN));
                while (ClientStateHolder.getInstance().getClientState().ordinal() >= ClientStateHolder.ClientState.LOGGED_IN.ordinal()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
            return null;
        }
    }
}
