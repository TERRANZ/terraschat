package ru.terra.tschat.shared.constants;

public interface OpCodes {

    int ISOpCodesStart = 10000;
    int LoginOpcodeStart = 1;
    int LoginOpcodeEnd = 10;
    int UserOpcodeStart = 11;
    int UserOpcodeEnd = 100;
    int ChatOpcodeStart = 201;
    int ChatOpcodeEnd = 300;


    interface Client {
        interface Login {
            int CMSG_LOGIN = 2;
            int CMSG_LOGOUT = 3;
            int CSMG_BOOT_ME = 4;
            int CMSG_REG = 5;
            int CMSG_UNREG = 6;
        }

        interface User {
            int CMSG_GET_CONTACTS = 12;
            int CMSG_ADD_CONTACT = 13;
            int CMSG_DEL_CONTACT = 14;
            int CMSG_GET_CONTACT_INFO = 15;
        }

        interface Chat {
            int CMSG_SAY = 202;
            int CMSG_WISP = 203;
        }
    }

    interface Server {
        int SMSG_OK = 501;
        int SMSG_USER_BOOT = 502;
        int SMSG_UPDATE = 503;

        interface Chat {
            int SMSG_CHAT_MESSAGE = 504;
        }

        interface User {
            int SMSG_USER_CONTACTS = 505;
            int SMSG_CONTACT_INFO = 507;
        }

        interface Login {
            int SMSG_LOGIN_FAILED = 506;
        }
    }

    interface InterServer {
        int ISMSG_HELLO = 10001;
        int ISMSG_REG = 10002;
        int ISMSG_UNREG = 10003;
        int ISMSG_BOOT_USER = 10004;
        int ISMSG_USER_REG = 10005;
        int ISMSG_UNREG_USER = 10008;
        int ISMSG_UPDATE_USER = 10010;
        int ISMSG_PING = 10011;
    }
}
