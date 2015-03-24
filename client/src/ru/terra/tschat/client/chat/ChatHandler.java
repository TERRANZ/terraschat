package ru.terra.tschat.client.chat;

import ru.terra.tschat.shared.packet.server.chat.ChatMessagePacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 24.03.15
 * Time: 12:30
 */
public class ChatHandler {
    private static ChatHandler instance = new ChatHandler();
    private List<ChatNotifier> notifierList = new ArrayList<>();


    private ChatHandler() {
    }

    public static ChatHandler getInstance() {
        return instance;
    }

    public void addNotifier(ChatNotifier notifier) {
        notifierList.add(notifier);
    }

    public void removeNotifier(ChatNotifier notifier) {
        notifierList.remove(notifier);
    }

    public void notify(ChatMessagePacket packet) {
        for (ChatNotifier cn : notifierList)
            cn.onChatEvent(packet);
    }
}
