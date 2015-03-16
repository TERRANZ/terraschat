package ru.terra.tschat.server.loginserver;

import org.apache.log4j.BasicConfigurator;
import ru.terra.tschat.interserver.network.NetworkManager;
import ru.terra.tschat.shared.config.Config;
import ru.terra.tschat.shared.config.ConfigConstants;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        NetworkManager.getInstance().start(LoginWorker.class,
                Config.getConfig().getValue(ConfigConstants.FRONTEND_IP, ConfigConstants.FRONTEND_IP_DEFAULT),
                12346);
    }

}
