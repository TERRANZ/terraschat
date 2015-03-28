package ru.terra.tschat.server.frontend.server;

import ru.terra.tschat.shared.entity.UserInfo;

/**
 * Date: 28.03.15
 * Time: 12:53
 */
public class FrontendUser extends UserInfo {
    public static enum FEUserState {
        INIT, CONNECTED, TEMP, LOGGED_IN;
    }

    public FEUserState state = FEUserState.INIT;
}
