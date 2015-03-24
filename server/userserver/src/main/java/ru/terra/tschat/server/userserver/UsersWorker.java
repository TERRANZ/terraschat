package ru.terra.tschat.server.userserver;

import org.apache.log4j.Logger;
import ru.terra.tschat.interserver.network.netty.InterserverWorker;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.client.contacts.AddContactPacket;
import ru.terra.tschat.shared.packet.interserver.HelloPacket;
import ru.terra.tschat.shared.packet.interserver.RegisterPacket;
import ru.terra.tschat.shared.packet.server.UserBootPacket;
import ru.terra.tschat.shared.persistance.UserLoader;
import ru.terra.tschat.shared.persistance.UserSaver;
import ru.terra.tschat.shared.persistance.impl.JsonUserLoaderImpl;
import ru.terra.tschat.shared.persistance.impl.JsonUserSaverImpl;

import java.util.LinkedList;
import java.util.List;

public class UsersWorker extends InterserverWorker {

    private Logger log = Logger.getLogger(this.getClass());
    private List<UserInfo> users = new LinkedList<>();
    private UserLoader userLoader = new JsonUserLoaderImpl();
    private UserSaver userSaver = new JsonUserSaverImpl();
    private UsersHandler handler = UsersHandler.getInstance();

    @Override
    public void disconnectedFromChannel() {
        log.info("Frontend disconnected us");
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
        switch (packet.getOpCode()) {
            case OpCodes.InterServer.ISMSG_HELLO: {
                HelloPacket helloPacket = new HelloPacket();
                helloPacket.setHello("user server");
                RegisterPacket registerPacket = new RegisterPacket();
                registerPacket.setStartRange(OpCodes.UserOpcodeStart);
                registerPacket.setEndRange(OpCodes.UserOpcodeEnd);
                networkManager.sendPacket(helloPacket);
                networkManager.sendPacket(registerPacket);
            }
            break;
            case OpCodes.InterServer.ISMSG_BOOT_USER: {
                log.info("Registering user with uid = " + packet.getSender());
                UserInfo userInfo = userLoader.loadUser(packet.getSender());
                UserBootPacket userBootPacket = new UserBootPacket();
                userBootPacket.setUserInfo(userInfo);
                userBootPacket.setSender(packet.getSender());
                networkManager.sendPacket(userBootPacket);
                users.add(userInfo);
            }
            break;
            case OpCodes.InterServer.ISMSG_UNREG_USER: {
                log.info("Unregistering user with uid = " + packet.getSender());
                UserInfo playerInfoToRemove = null;
                for (UserInfo playerInfo : users)
                    if (playerInfo.getUID().equals(packet.getSender())) {
                        userSaver.save(playerInfo);
                        playerInfoToRemove = playerInfo;
                    }
                if (playerInfoToRemove != null)
                    users.remove(playerInfoToRemove);
            }
            break;
            case OpCodes.Client.User.CMSG_GET_CONTACTS: {
                handler.getContacts(getUser(packet.getSender()));
            }
            break;
            case OpCodes.Client.User.CMSG_ADD_CONTACT: {
                handler.addContact(getUser(packet.getSender()), ((AddContactPacket) packet).getContactUID());
            }
            break;
        }
    }

    private UserInfo getUser(Long guid) {
        for (UserInfo playerInfo : users)
            if (playerInfo.getUID().equals(guid))
                return playerInfo;
        return null;
    }
}
