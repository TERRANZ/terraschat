package ru.terra.tschat.client.network;

import ru.terra.tschat.client.framework.ClientStateHolder;
import ru.terra.tschat.interserver.network.NetworkManager;
import ru.terra.tschat.shared.packet.client.chat.ClientSayPacket;
import ru.terra.tschat.shared.packet.client.contacts.GetContactInfoPacket;
import ru.terra.tschat.shared.packet.client.contacts.GetContactsPacket;
import ru.terra.tschat.shared.packet.client.login.LoginPacket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Date: 18.06.15
 * Time: 14:13
 */
public class NetworkHelper {

    private static ExecutorService service = Executors.newFixedThreadPool(2);

    public static void sendLogin(final String login, final String pass) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.LOGIN);
                LoginPacket loginPacket = new LoginPacket();
                loginPacket.setLogin(login);
                loginPacket.setPassword(pass);
                loginPacket.setSender(GUIDHOlder.getInstance().getGuid());
                NetworkManager.getInstance().sendPacket(loginPacket);
            }
        });
    }

    public static void sendMessage(final String msg, final long to) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                ClientSayPacket packet = new ClientSayPacket(msg, to);
                packet.setSender(GUIDHOlder.getInstance().getGuid());
                NetworkManager.getInstance().sendPacket(packet);
            }
        });
    }

    public static void sendContactsInfoRequest() {
        service.submit(new Runnable() {
            @Override
            public void run() {
                NetworkManager.getInstance().sendPacket(new GetContactsPacket());
            }
        });
    }

    public static void sendUserInfoRequest(final long uid) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                NetworkManager.getInstance().sendPacket(new GetContactInfoPacket(uid));
            }
        });
    }
}
