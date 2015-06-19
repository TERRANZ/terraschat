package ru.terra.tschat.client.test;

import org.apache.log4j.BasicConfigurator;
import ru.terra.tschat.client.chat.ChatHandler;
import ru.terra.tschat.client.chat.ChatNotifier;
import ru.terra.tschat.client.framework.ClientStateHolder;
import ru.terra.tschat.client.framework.ClientWorker;
import ru.terra.tschat.client.network.GUIDHOlder;
import ru.terra.tschat.interserver.network.NetworkManager;
import ru.terra.tschat.shared.context.SharedContext;
import ru.terra.tschat.shared.core.Logger;
import ru.terra.tschat.shared.packet.client.chat.ClientSayPacket;
import ru.terra.tschat.shared.packet.client.login.LoginPacket;
import ru.terra.tschat.shared.packet.server.chat.ChatMessagePacket;

import java.util.Date;
import java.util.UUID;

/**
 * Date: 27.03.15
 * Time: 20:42
 */
public class Main {
    public static void main(String... args) {
        BasicConfigurator.configure();
        SharedContext.getInstance().setLogger(new Logger() {
            @Override
            public void error(String tag, String msg) {
                org.apache.log4j.Logger.getLogger(Main.class).error(tag + ":" + msg);
            }

            @Override
            public void error(String tag, String msg, Throwable t) {
                org.apache.log4j.Logger.getLogger(Main.class).error(tag + ":" + msg, t);
            }

            @Override
            public void info(String tag, String msg) {
                org.apache.log4j.Logger.getLogger(Main.class).info(tag + ":" + msg);
            }

            @Override
            public void debug(String tag, String msg) {
                org.apache.log4j.Logger.getLogger(Main.class).debug(tag + ":" + msg);
            }
        });

        ChatHandler.getInstance().addNotifier(new ChatNotifier() {
            @Override
            public void onChatEvent(ChatMessagePacket chatMessagePacket) {
                org.apache.log4j.Logger.getLogger(Main.class).info("Chat event: " + chatMessagePacket.getMsg());
            }
        });

        new Thread(new TestRunnable()).start();
    }


    private static class TestRunnable implements Runnable {
        @Override
        public void run() {
            ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.INIT);
            NetworkManager.getInstance().start(ClientWorker.class, "terranout.ath.cx", 12345);
            while (!ClientStateHolder.getInstance().getClientState().equals(ClientStateHolder.ClientState.LOGIN))
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//            ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.LOGIN);
            LoginPacket loginPacket = new LoginPacket();
            loginPacket.setLogin(UUID.randomUUID().toString());
            loginPacket.setPassword(UUID.randomUUID().toString());
            org.apache.log4j.Logger.getLogger(Main.class).info("Loggin in with guid: " + GUIDHOlder.getInstance().getGuid());
            loginPacket.setSender(GUIDHOlder.getInstance().getGuid());
            NetworkManager.getInstance().sendPacket(loginPacket);

            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ClientSayPacket packet = new ClientSayPacket(new Date().toString(), 0L);
                packet.setSender(GUIDHOlder.getInstance().getGuid());
                NetworkManager.getInstance().sendPacket(packet);
            }
        }
    }
}