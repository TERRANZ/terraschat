package ru.terra.tschat.client.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import ru.terra.tschat.client.R;
import ru.terra.tschat.client.chat.ClientStateHolder;
import ru.terra.tschat.client.network.ClientWorker;
import ru.terra.tschat.client.network.GUIDHOlder;
import ru.terra.tschat.client.util.AndroidClassSearcher;
import ru.terra.tschat.interserver.network.NetworkManager;
import ru.terra.tschat.shared.context.SharedContext;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.client.ChatSayPacket;
import ru.terra.tschat.shared.packet.client.LoginPacket;

import java.util.Date;
import java.util.UUID;

/**
 * Date: 18.03.15
 * Time: 17:40
 */
public class ChatService extends IntentService {
    public ChatService() {
        super("Chat background service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedContext.getInstance().setClassSearcher(new AndroidClassSearcher<AbstractPacket>(getApplicationContext()));
        ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.INIT);
        NetworkManager.getInstance().start(ClientWorker.class, getString(R.string.ip), 12345);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.LOGIN);
        LoginPacket loginPacket = new LoginPacket();
        loginPacket.setLogin(prefs.getString(getString(R.string.login), UUID.randomUUID().toString()));
        loginPacket.setPassword(prefs.getString(getString(R.string.pass), UUID.randomUUID().toString()));
        loginPacket.setSender(GUIDHOlder.getInstance().getGuid());
        NetworkManager.getInstance().sendPacket(loginPacket);



        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ChatSayPacket packet = new ChatSayPacket("Current time is " + new Date().getTime(), 0L);
            packet.setSender(GUIDHOlder.getInstance().getGuid());
            NetworkManager.getInstance().sendPacket(packet);
        }
    }
}
