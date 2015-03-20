package ru.terra.tschat.client.chat;


public class ClientStateHolder {
    private ChatStateChangeNotifier notifier;

    public static enum ClientState {
        INIT, LOGIN, LOGGED_IN, CHAR_BOOT, IN_CHAT;
    }

    private ClientState clientState = ClientState.INIT;
    private static ClientStateHolder instance = new ClientStateHolder();

    private ClientStateHolder() {
    }

    public static ClientStateHolder getInstance() {
        synchronized (instance) {
            return instance;
        }
    }

    public ClientState getClientState() {
        synchronized (clientState) {
            return clientState;
        }
    }

    public void setClientState(ClientState clientState) {
        synchronized (clientState) {
            if (notifier != null)
                notifier.onGameStateChange(getClientState(), clientState);
            this.clientState = clientState;
        }
    }

    public ChatStateChangeNotifier getNotifier() {
        return notifier;
    }

    public void setNotifier(ChatStateChangeNotifier notifier) {
        this.notifier = notifier;
    }
}
