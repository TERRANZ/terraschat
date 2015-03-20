package ru.terra.tschat.server.userserver;

import org.apache.log4j.Logger;
import ru.terra.tschat.interserver.network.netty.InterserverWorker;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.interserver.HelloPacket;
import ru.terra.tschat.shared.packet.interserver.RegisterPacket;
import ru.terra.tschat.shared.packet.server.UserBootPacket;
import ru.terra.tschat.shared.persistance.CharSaver;
import ru.terra.tschat.shared.persistance.UserLoader;
import ru.terra.tschat.shared.persistance.impl.JsonCharLoaderImpl;
import ru.terra.tschat.shared.persistance.impl.JsonCharSaverImpl;

import java.util.LinkedList;
import java.util.List;

public class UsersWorker extends InterserverWorker {

    private Logger log = Logger.getLogger(this.getClass());
    private List<UserInfo> players = new LinkedList<>();
    private UserLoader charLoader = new JsonCharLoaderImpl();
    private CharSaver charSaver = new JsonCharSaverImpl();

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
                log.info("Registering character with uid = " + packet.getSender());
                UserInfo userInfo = charLoader.loadUser(packet.getSender());
                UserBootPacket userBootPacket = new UserBootPacket();
                userBootPacket.setUserInfo(userInfo);
                userBootPacket.setSender(packet.getSender());
                networkManager.sendPacket(userBootPacket);
                players.add(userInfo);
            }
            break;
            case OpCodes.InterServer.ISMSG_UNREG_USER: {
                log.info("Unregistering char with uid = " + packet.getSender());
                UserInfo playerInfoToRemove = null;
                for (UserInfo playerInfo : players)
                    if (playerInfo.getUID().equals(packet.getSender())) {
                        charSaver.save(playerInfo);
                        playerInfoToRemove = playerInfo;
                    }
                if (playerInfoToRemove != null)
                    players.remove(playerInfoToRemove);

            }
            break;
        }
    }

}
