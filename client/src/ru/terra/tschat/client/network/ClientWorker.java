package ru.terra.tschat.client.network;

import android.util.Log;
import ru.terra.tschat.client.chat.ChatHandler;
import ru.terra.tschat.client.network.client.ClientManager;
import ru.terra.tschat.client.network.client.ClientStateHolder;
import ru.terra.tschat.interserver.network.NetworkManager;
import ru.terra.tschat.interserver.network.netty.InterserverWorker;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.client.BootMePacket;
import ru.terra.tschat.shared.packet.server.LoginFailedPacket;
import ru.terra.tschat.shared.packet.server.UserBootPacket;
import ru.terra.tschat.shared.packet.server.chat.ChatMessagePacket;

/**
 * User: Vadim Korostelev
 * Date: 30.08.13
 * Time: 16:15
 */
public class ClientWorker extends InterserverWorker {

    @Override
    public void disconnectedFromChannel() {
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
        switch (packet.getOpCode()) {
            case OpCodes.Server.SMSG_OK: {
                ClientStateHolder.ClientState clientState = ClientStateHolder.getInstance().getClientState();
                switch (clientState) {
                    case INIT:
                        GUIDHOlder.getInstance().setGuid(packet.getSender());
                        break;
                    case LOGIN:
                        GUIDHOlder.getInstance().setGuid(packet.getSender());
                        BootMePacket bootMePacket = new BootMePacket();
                        bootMePacket.setSender(GUIDHOlder.getInstance().getGuid());
                        NetworkManager.getInstance().sendPacket(bootMePacket);
                        ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.LOGGED_IN);
                        break;
                    case LOGGED_IN:
                        break;
                    case IN_CHAT:
                        ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.IN_CHAT);
                        break;
                }
            }
            break;
            case OpCodes.Server.SMSG_CHAR_BOOT: {
                ClientManager.getInstance().setUserInfo(((UserBootPacket) packet).getPlayerInfo());
                ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.CHAR_BOOT);
            }
            break;
            case OpCodes.Server.Login.SMSG_LOGIN_FAILED: {
                Log.e("ClientWorker", "Unable to login: " + ((LoginFailedPacket) packet).getReason());
            }
            break;
            case OpCodes.Server.Chat.SMSG_CHAT_MESSAGE: {
                ChatHandler.getInstance().notify((ChatMessagePacket) packet);
            }
            break;
            case OpCodes.Server.User.SMSG_USER_CONTACTS: {
            }
            break;
        }
    }
}
