package ru.terra.tschat.server.loginserver;

import org.apache.log4j.BasicConfigurator;
import ru.terra.tschat.interserver.network.NetworkManager;
import ru.terra.tschat.shared.config.Config;
import ru.terra.tschat.shared.config.ConfigConstants;
import ru.terra.tschat.shared.context.SharedContext;
import ru.terra.tschat.shared.core.Logger;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        SharedContext.getInstance().setLogger(new Logger() {
            @Override
            public void error(String tag, String msg) {
                org.apache.log4j.Logger.getLogger(Main.class).error(tag + ":" + msg);
            }

            @Override
            public void error(String tag, String msg, Throwable t) {
                org.apache.log4j.Logger.getLogger(Main.class).error(tag + ":" + msg, t);
            }

            @Override
            public void info(String tag, String msg) {
                org.apache.log4j.Logger.getLogger(Main.class).info(tag + ":" + msg);
            }

            @Override
            public void debug(String tag, String msg) {
                org.apache.log4j.Logger.getLogger(Main.class).debug(tag + ":" + msg);
            }
        });
        NetworkManager.getInstance().start(LoginWorker.class,
                Config.getConfig().getValue(ConfigConstants.FRONTEND_IP, ConfigConstants.FRONTEND_IP_DEFAULT),
                12346);
    }

}
