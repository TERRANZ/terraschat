package ru.terra.tschat.client.network.client;

/**
 * Date: 12.11.14
 * Time: 15:43
 */
public interface ClientStateChangeNotifier {
    public void onClientStateChange(ClientStateHolder.ClientState oldgs, ClientStateHolder.ClientState newgs);
}
