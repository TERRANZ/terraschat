package ru.terra.tschat.server.frontend;

import org.apache.log4j.BasicConfigurator;
import ru.terra.tschat.server.frontend.network.ServerThread;
import ru.terra.tschat.server.frontend.server.FrontEndServerWorker;
import ru.terra.tschat.server.frontend.server.InterserverFEWorker;
import ru.terra.tschat.shared.context.SharedContext;
import ru.terra.tschat.shared.core.Logger;

/**
 * Date: 16.03.15
 * Time: 18:44
 */
public class Main {
    public static void main(String... args) {
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
        new Thread(new ServerThread(12345, "0.0.0.0", FrontEndServerWorker.class)).start();
        new Thread(new ServerThread(12346, "0.0.0.0", InterserverFEWorker.class)).start();
    }
}
