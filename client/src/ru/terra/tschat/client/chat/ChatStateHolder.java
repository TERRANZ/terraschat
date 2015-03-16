package ru.terra.tschat.client.chat;


public class ChatStateHolder {
    private ChatStateChangeNotifier notifier;

    public static enum GameState {
        INIT, LOGIN, LOGGED_IN, CHAR_BOOT, SERVER_SELECTED, IN_WORLD;
    }

    private GameState gameState = GameState.INIT;
    private static ChatStateHolder instance = new ChatStateHolder();

    private ChatStateHolder() {
    }

    public static ChatStateHolder getInstance() {
        synchronized (instance) {
            return instance;
        }
    }

    public GameState getGameState() {
        synchronized (gameState) {
            return gameState;
        }
    }

    public void setGameState(GameState gameState) {
        synchronized (gameState) {
            if (notifier != null)
                notifier.onGameStateChange(getGameState(), gameState);
            this.gameState = gameState;
        }
    }

    public ChatStateChangeNotifier getNotifier() {
        return notifier;
    }

    public void setNotifier(ChatStateChangeNotifier notifier) {
        this.notifier = notifier;
    }
}
