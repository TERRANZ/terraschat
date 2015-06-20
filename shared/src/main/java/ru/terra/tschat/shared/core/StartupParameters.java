package ru.terra.tschat.shared.core;

import com.beust.jcommander.Parameter;

/**
 * Date: 19.06.15
 * Time: 18:29
 */
public class StartupParameters {
    @Parameter(names = {"-f", "--frontend"}, description = "Frontend server address")
    private String properties = "terranout.ath.cx";
    @Parameter(names = {"-r", "--reconnect"}, description = "Automatic reconnect")
    private Boolean reconnect = false;


    private static StartupParameters instance = new StartupParameters();

    private StartupParameters() {
    }

    public static StartupParameters getInstance() {
        return instance;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public Boolean getReconnect() {
        return reconnect;
    }

    public void setReconnect(Boolean reconnect) {
        this.reconnect = reconnect;
    }
}
