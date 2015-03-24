package ru.terra.tschat.client.service;

import android.app.IntentService;
import android.content.*;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import ru.terra.tschat.client.R;
import ru.terra.tschat.client.network.client.ClientStateChangeNotifier;
import ru.terra.tschat.client.network.client.ClientStateHolder;
import ru.terra.tschat.client.network.ClientWorker;
import ru.terra.tschat.client.network.GUIDHOlder;
import ru.terra.tschat.client.util.AndroidClassSearcher;
import ru.terra.tschat.interserver.network.NetworkManager;
import ru.terra.tschat.shared.context.SharedContext;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.client.chat.ClientSayPacket;
import ru.terra.tschat.shared.packet.client.login.LoginPacket;

import java.util.Date;
import java.util.UUID;

/**
 * Date: 18.03.15
 * Time: 17:40
 */
public class ChatService extends IntentService {

    public static final String CHAT_SERVICE_RECEIVER = " ru.terra.tschat.client.service.chat_service_receiver";
    public static final String PARAM_DO = "do";
    public static final int DO_CONNECT = 1;
    public static final int DO_LOGIN = 2;
    public static final int DO_REG = 3;
    public static final int DO_SAY = 4;


    private ChatServiceReceiver chatReceiver;
    private SharedPreferences prefs;

    public ChatService() {
        super("Chat background service");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter statusFilter = new IntentFilter(CHAT_SERVICE_RECEIVER);
        statusFilter.addCategory(Intent.CATEGORY_DEFAULT);
        chatReceiver = new ChatServiceReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(chatReceiver, statusFilter);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedContext.getInstance().setClassSearcher(new AndroidClassSearcher<AbstractPacket>(getApplicationContext()));
        ClientStateHolder.getInstance().setNotifier(new MyClientStateChangeNotifier());
        ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.INIT);
        NetworkManager.getInstance().start(ClientWorker.class, getString(R.string.ip), 12345);

        while (true) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class ChatServiceReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getIntExtra(PARAM_DO, 0)) {
                case DO_LOGIN: {
                    ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.LOGIN);
                    LoginPacket loginPacket = new LoginPacket();
                    loginPacket.setLogin(prefs.getString(getString(R.string.login), UUID.randomUUID().toString()));
                    loginPacket.setPassword(prefs.getString(getString(R.string.pass), UUID.randomUUID().toString()));
                    loginPacket.setSender(GUIDHOlder.getInstance().getGuid());
                    NetworkManager.getInstance().sendPacket(loginPacket);
                }
                break;
                case DO_REG: {
                }
                break;
                case DO_SAY: {
                    ClientSayPacket packet = new ClientSayPacket(new Date() + " " + intent.getStringExtra("msg"), 0L);
                    packet.setSender(GUIDHOlder.getInstance().getGuid());
                    NetworkManager.getInstance().sendPacket(packet);
                }
                break;
            }
        }
    }

    private class MyClientStateChangeNotifier implements ClientStateChangeNotifier {
        @Override
        public void onClientStateChange(ClientStateHolder.ClientState oldgs, ClientStateHolder.ClientState newgs) {
            if (newgs.equals(ClientStateHolder.ClientState.LOGGED_IN)) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(getString(R.string.loggedIn), true);
                editor.commit();
            }
        }
    }
}
