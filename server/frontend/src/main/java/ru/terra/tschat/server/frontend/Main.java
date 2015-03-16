package ru.terra.tschat.server.frontend;

import org.apache.log4j.BasicConfigurator;
import ru.terra.tschat.server.frontend.network.ServerThread;
import ru.terra.tschat.server.frontend.server.FrontEndServerWorker;
import ru.terra.tschat.server.frontend.server.InterserverFEWorker;

/**
 * Date: 16.03.15
 * Time: 18:44
 */
public class Main {
    public static void main(String... args) {
        BasicConfigurator.configure();
        new Thread(new ServerThread(12345, "0.0.0.0", FrontEndServerWorker.class)).start();
        new Thread(new ServerThread(12346, "0.0.0.0", InterserverFEWorker.class)).start();
    }
}
