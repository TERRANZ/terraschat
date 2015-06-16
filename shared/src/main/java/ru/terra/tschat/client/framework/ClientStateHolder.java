package ru.terra.tschat.client.framework;


public class ClientStateHolder {
    private ClientStateChangeNotifier notifier;

    public static enum ClientState {
        INIT, LOGIN, LOGGED_IN, USER_BOOT, IN_CHAT;
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
                notifier.onClientStateChange(getClientState(), clientState);
            this.clientState = clientState;
        }
    }

    public ClientStateChangeNotifier getNotifier() {
        return notifier;
    }

    public void setNotifier(ClientStateChangeNotifier notifier) {
        this.notifier = notifier;
    }
}
