package ru.terra.tschat.server.loginserver;

import org.apache.log4j.Logger;
import ru.terra.tschat.interserver.network.netty.InterserverWorker;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.client.LoginPacket;
import ru.terra.tschat.shared.packet.interserver.BootCharPacket;
import ru.terra.tschat.shared.packet.interserver.UserRegPacket;
import ru.terra.tschat.shared.packet.interserver.HelloPacket;
import ru.terra.tschat.shared.packet.interserver.RegisterPacket;
import ru.terra.tschat.shared.packet.server.LoginFailedPacket;
import ru.terra.tschat.shared.packet.server.OkPacket;
import ru.terra.tschat.shared.persistance.UserLoader;
import ru.terra.tschat.shared.persistance.impl.JsonCharLoaderImpl;

public class LoginWorker extends InterserverWorker {

    private Logger logger = Logger.getLogger(this.getClass());
    private UserLoader charLoader = new JsonCharLoaderImpl();

    @Override
    public void disconnectedFromChannel() {
        logger.info("Frontend disconnected us");
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
        switch (packet.getOpCode()) {
            case OpCodes.InterServer.ISMSG_HELLO: {
                HelloPacket helloPacket = new HelloPacket();
                helloPacket.setHello("login server");
                RegisterPacket registerPacket = new RegisterPacket();
                registerPacket.setStartRange(OpCodes.LoginOpcodeStart);
                registerPacket.setEndRange(OpCodes.LoginOpcodeEnd);
                networkManager.sendPacket(helloPacket);
                networkManager.sendPacket(registerPacket);
            }
            break;
            case OpCodes.Client.Login.CMSG_LOGIN: {
                LoginPacket loginPacket = (LoginPacket) packet;
                logger.info("Client with login " + loginPacket.getLogin() + " and pass " + loginPacket.getPassword() + " attempting to log in");
                Long uid = charLoader.findUser(loginPacket.getLogin(), loginPacket.getPassword());
                logger.info("Client with id " + loginPacket.getSender() + " logged in");
                if (uid != null) {
                    logger.info("Client with id " + loginPacket.getSender() + " logged in");
                    logger.info("Client registered with GUID = " + uid);
                    OkPacket okPacket = new OkPacket();
                    okPacket.setSender(uid);
                    UserRegPacket userRegPacket = new UserRegPacket();
                    userRegPacket.setSender(uid);
                    userRegPacket.setOldId(loginPacket.getSender());
                    networkManager.sendPacket(userRegPacket);
                    networkManager.sendPacket(okPacket);
                } else {
                    logger.info("Unable to find character");
                    LoginFailedPacket loginFailedPacket = new LoginFailedPacket();
                    loginFailedPacket.setSender(loginPacket.getSender());
                    loginFailedPacket.setReason("Unable to find user by password and login");
                    networkManager.sendPacket(loginFailedPacket);
                }
            }
            break;
            case OpCodes.Client.Login.CSMG_BOOT_ME: {
                logger.info("Client sent Boot Me to us");
                BootCharPacket bootCharPacket = new BootCharPacket();
                bootCharPacket.setSender(packet.getSender());
                networkManager.sendPacket(bootCharPacket);
            }
            break;
            case OpCodes.InterServer.ISMSG_UNREG_USER: {
                logger.info("Unregistering char with uid = " + packet.getSender());
            }
            break;
        }
    }

}
