package ru.terra.tschat.server.loginserver;

import org.apache.log4j.Logger;
import ru.terra.tschat.interserver.network.netty.InterserverWorker;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.client.login.LoginPacket;
import ru.terra.tschat.shared.packet.client.login.LogoutPacket;
import ru.terra.tschat.shared.packet.client.login.RegPacket;
import ru.terra.tschat.shared.packet.client.login.UnregPacket;
import ru.terra.tschat.shared.packet.interserver.BootCharPacket;
import ru.terra.tschat.shared.packet.interserver.HelloPacket;
import ru.terra.tschat.shared.packet.interserver.RegisterPacket;

public class LoginWorker extends InterserverWorker {

    private Logger logger = Logger.getLogger(this.getClass());
    private LoginHandler loginHandler = LoginHandler.getInstance();

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
            case OpCodes.InterServer.ISMSG_UNREG_USER: {
                logger.info("Unregistering char with uid = " + packet.getSender());
            }
            break;


            case OpCodes.Client.Login.CMSG_LOGIN: {
                loginHandler.handleLogin((LoginPacket) packet);
            }
            break;
            case OpCodes.Client.Login.CMSG_LOGOUT: {
                loginHandler.handleLogout((LogoutPacket) packet);
            }
            break;
            case OpCodes.Client.Login.CMSG_REG: {
                loginHandler.handleReg((RegPacket) packet);
            }
            break;
            case OpCodes.Client.Login.CMSG_UNREG: {
                loginHandler.handleUnreg((UnregPacket) packet);
            }
            break;

            case OpCodes.Client.Login.CSMG_BOOT_ME: {
                logger.info("Client sent Boot Me to us");
                BootCharPacket bootCharPacket = new BootCharPacket();
                bootCharPacket.setSender(packet.getSender());
                networkManager.sendPacket(bootCharPacket);
            }
            break;
        }
    }

}
