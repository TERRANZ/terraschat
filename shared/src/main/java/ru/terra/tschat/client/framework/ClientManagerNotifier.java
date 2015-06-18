package ru.terra.tschat.client.framework;

/**
 * Date: 18.06.15
 * Time: 15:09
 */
public interface ClientManagerNotifier {
    public static enum ClientManagerEvent {
        CONTACT_INFO;
    }

    public void onEvent(ClientManagerEvent event);
}
