package ru.terra.tschat.shared.constants;

public interface OpCodes {

    public static final int ISOpCodesStart = 10000;
    public static final int LoginOpcodeStart = 1;
    public static final int LoginOpcodeEnd = 10;
    public static final int UserOpcodeStart = 11;
    public static final int UserOpcodeEnd = 100;
    public static final int ChatOpcodeStart = 201;
    public static final int ChatOpcodeEnd = 300;


    public interface Client {
        public interface Login {
            public static final int CMSG_LOGIN = 2;
            public static final int CMSG_LOGOUT = 3;
            public static final int CSMG_BOOT_ME = 4;
            public static final int CMSG_REG = 5;
            public static final int CMSG_UNREG = 6;
        }

        public interface User {
            public static final int CMSG_GET_CONTACTS = 12;
            public static final int CMSG_ADD_CONTACT = 13;
            public static final int CMSG_DEL_CONTACT = 14;
            public static final int CMSG_GET_CONTACT_INFO = 15;
        }

        public interface Chat {
            public static final int CMSG_SAY = 202;
            public static final int CMSG_WISP = 203;
        }
    }

    public interface Server {
        public static final int SMSG_OK = 501;
        public static final int SMSG_CHAR_BOOT = 502;
        public static final int SMSG_UPDATE = 503;

        public interface Chat {
            public static final int SMSG_CHAT_MESSAGE = 504;
        }

        public interface User {
            public static final int SMSG_USER_CONTACTS = 505;
            public static final int SMSG_CONTACT_INFO = 507;
        }

        public interface Login {
            public static final int SMSG_LOGIN_FAILED = 506;
        }
    }

    public interface InterServer {
        public static final int ISMSG_HELLO = 10001;
        public static final int ISMSG_REG = 10002;
        public static final int ISMSG_UNREG = 10003;
        public static final int ISMSG_BOOT_USER = 10004;
        public static final int ISMSG_USER_REG = 10005;
        public static final int ISMSG_UNREG_USER = 10008;
        public static final int ISMSG_UPDATE_USER = 10010;

    }
}
