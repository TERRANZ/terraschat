package ru.terra.tschat.client.network;

import ru.terra.tschat.client.chat.ChatManager;
import ru.terra.tschat.client.chat.ChatStateHolder;
import ru.terra.tschat.interserver.network.NetworkManager;
import ru.terra.tschat.interserver.network.netty.InterserverWorker;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.client.BootMePacket;
import ru.terra.tschat.shared.packet.server.CharBootPacket;

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
                ChatStateHolder.GameState gs = ChatStateHolder.getInstance().getGameState();
                switch (gs) {
                    case INIT:
                        GUIDHOlder.getInstance().setGuid(packet.getSender());
                        break;
                    case LOGIN:
                        GUIDHOlder.getInstance().setGuid(packet.getSender());
                        BootMePacket bootMePacket = new BootMePacket();
                        bootMePacket.setSender(GUIDHOlder.getInstance().getGuid());
                        NetworkManager.getInstance().sendPacket(bootMePacket);
                        ChatStateHolder.getInstance().setGameState(ChatStateHolder.GameState.LOGGED_IN);
                        break;
                    case LOGGED_IN:
                        break;
                    case CHAR_BOOT:
                        break;
                    case SERVER_SELECTED:
                        ChatStateHolder.getInstance().setGameState(ChatStateHolder.GameState.IN_WORLD);
                        break;
                    case IN_WORLD:
                        break;
                }
            }
            break;
            case OpCodes.Server.SMSG_CHAR_BOOT: {
                ChatManager.getInstance().setUserInfo(((CharBootPacket) packet).getPlayerInfo());
//                GuiManager.getInstance().publicWorlds(((CharBootPacket) packet).getWorlds());
                ChatStateHolder.getInstance().setGameState(ChatStateHolder.GameState.CHAR_BOOT);
            }
            break;
            case OpCodes.Server.Login.SMSG_LOGIN_FAILED: {
//                logger.error("Unable to login: " + ((LoginFailedPacket) packet).getReason());
            }
            break;
        }
    }
}
