package ru.terra.tschat.client.framework;

import ru.terra.tschat.client.chat.ChatHandler;
import ru.terra.tschat.client.network.GUIDHOlder;
import ru.terra.tschat.interserver.network.NetworkManager;
import ru.terra.tschat.interserver.network.netty.InterserverWorker;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.context.SharedContext;
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
                SharedContext.getInstance().getLogger().error("ClientWorker", "server said OK when client state: " + clientState.name());
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
                        ClientStateHolder.getInstance().setClientState(ClientStateHolder.ClientState.IN_CHAT);
                        break;
                    case CHAR_BOOT:
                        break;
                    case IN_CHAT:
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
                SharedContext.getInstance().getLogger().error("ClientWorker", "Unable to login: " + ((LoginFailedPacket) packet).getReason());
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
