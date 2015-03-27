package ru.terra.tschat.server.loginserver;

import org.apache.log4j.Logger;
import ru.terra.tschat.interserver.network.NetworkManager;
import ru.terra.tschat.shared.packet.client.login.LoginPacket;
import ru.terra.tschat.shared.packet.client.login.LogoutPacket;
import ru.terra.tschat.shared.packet.client.login.RegPacket;
import ru.terra.tschat.shared.packet.client.login.UnregPacket;
import ru.terra.tschat.shared.packet.interserver.UserRegPacket;
import ru.terra.tschat.shared.packet.server.LoginFailedPacket;
import ru.terra.tschat.shared.packet.server.OkPacket;
import ru.terra.tschat.shared.persistance.UserLoader;
import ru.terra.tschat.shared.persistance.impl.JsonUserLoaderImpl;

/**
 * Date: 20.03.15
 * Time: 15:35
 */
public class LoginHandler {
    private static LoginHandler instance = new LoginHandler();
    private Logger logger = Logger.getLogger(this.getClass());
    private UserLoader charLoader = new JsonUserLoaderImpl();
    private NetworkManager networkManager = NetworkManager.getInstance();

    private LoginHandler() {
    }

    public static LoginHandler getInstance() {
        return instance;
    }

    public void handleLogin(LoginPacket loginPacket) {
        logger.info("Client with login " + loginPacket.getLogin() + " and pass " + loginPacket.getPassword() + " attempting to log in");
        Long uid = charLoader.findUser(loginPacket.getLogin(), loginPacket.getPassword());
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
            logger.info("Unable to find user");
            LoginFailedPacket loginFailedPacket = new LoginFailedPacket();
            loginFailedPacket.setSender(loginPacket.getSender());
            loginFailedPacket.setReason("Unable to find user by password and login");
            networkManager.sendPacket(loginFailedPacket);
        }
    }

    public void handleLogout(LogoutPacket packet) {
    }

    public void handleReg(RegPacket packet) {
    }

    public void handleUnreg(UnregPacket packet) {
    }

}
