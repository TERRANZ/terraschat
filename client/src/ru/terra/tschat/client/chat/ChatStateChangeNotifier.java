package ru.terra.tschat.client.chat;

/**
 * Date: 12.11.14
 * Time: 15:43
 */
public interface ChatStateChangeNotifier {
    public void onGameStateChange(ChatStateHolder.GameState oldgs, ChatStateHolder.GameState newgs);
}
