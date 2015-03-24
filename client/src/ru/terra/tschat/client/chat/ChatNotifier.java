package ru.terra.tschat.client.chat;

import ru.terra.tschat.shared.packet.server.chat.ChatMessagePacket;

/**
 * Date: 24.03.15
 * Time: 12:33
 */
public interface ChatNotifier {
    public void onChatEvent(ChatMessagePacket chatMessagePacket);
}
