package ru.terra.tschat.client.service;

import android.app.IntentService;
import android.content.Intent;
import ru.terra.tschat.client.chat.ClientStateHolder;
import ru.terra.tschat.client.constants.C;
import ru.terra.tschat.client.network.ClientWorker;
import ru.terra.tschat.client.network.GUIDHOlder;
import ru.terra.tschat.interserver.network.NetworkManager;
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
        ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.INIT);
        NetworkManager.getInstance().start(ClientWorker.class, C.ip, 12345);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.LOGIN);
        LoginPacket loginPacket = new LoginPacket();
        loginPacket.setLogin("mylogin " + UUID.randomUUID().toString());
        loginPacket.setPassword("mypass");
        loginPacket.setSender(GUIDHOlder.getInstance().getGuid());
        NetworkManager.getInstance().sendPacket(loginPacket);

        ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.IN_CHAT);

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
