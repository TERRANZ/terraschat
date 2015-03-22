package ru.terra.tschat.server.chatserver;

import org.apache.log4j.Logger;
import ru.terra.tschat.interserver.network.netty.InterserverWorker;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.client.chat.ChatSayPacket;
import ru.terra.tschat.shared.packet.interserver.HelloPacket;
import ru.terra.tschat.shared.packet.interserver.RegisterPacket;

public class ChatWorker extends InterserverWorker {

    private Logger log = Logger.getLogger(this.getClass());

    @Override
    public void disconnectedFromChannel() {
        log.info("Frontend disconnected us");
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
//        log.info("Received opcode "+packet.getOpCode());
        switch (packet.getOpCode()) {
            case OpCodes.InterServer.ISMSG_HELLO: {
                HelloPacket helloPacket = new HelloPacket();
                helloPacket.setHello("chat server");
                RegisterPacket registerPacket = new RegisterPacket();
                registerPacket.setStartRange(OpCodes.ChatOpcodeStart);
                registerPacket.setEndRange(OpCodes.ChatOpcodeEnd);
                networkManager.sendPacket(helloPacket);
                networkManager.sendPacket(registerPacket);
            }
            break;
            case OpCodes.InterServer.ISMSG_BOOT_USER: {
                log.info("Registering char with uid = " + packet.getSender());
            }
            break;
            case OpCodes.InterServer.ISMSG_UNREG_USER: {
                log.info("Unregistering char with uid = " + packet.getSender());
            }
            break;
            case OpCodes.Client.Chat.CMSG_SAY: {
                ChatSayPacket chatSayPacket = (ChatSayPacket) packet;
                log.info("Player " + packet.getSender() + " says: " + chatSayPacket.getMessage() + " to: " + chatSayPacket.getTo());
                chatSayPacket.setSender(chatSayPacket.getTo());
                networkManager.sendPacket(chatSayPacket);
            }
            break;
            case OpCodes.Client.Chat.CMSG_WISP: {
            }
            break;
        }
    }

}
