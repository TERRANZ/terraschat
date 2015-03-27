package ru.terra.tschat.shared.core;

/**
 * Date: 27.03.15
 * Time: 20:12
 */
public interface Logger {
    public void error(String tag, String msg);

    public void error(String tag, String msg, Throwable t);

    public void info(String tag, String msg);

    public void debug(String tag, String msg);
}
