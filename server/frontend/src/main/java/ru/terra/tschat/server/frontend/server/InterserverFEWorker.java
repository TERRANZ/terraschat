package ru.terra.tschat.server.frontend.server;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import ru.terra.tschat.server.frontend.network.netty.ServerWorker;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.interserver.UserRegPacket;
import ru.terra.tschat.shared.packet.interserver.HelloPacket;
import ru.terra.tschat.shared.packet.interserver.RegisterPacket;

public class InterserverFEWorker extends ServerWorker {

    private Logger log = Logger.getLogger(InterserverFEWorker.class);
    private ChannelsHolder channelsHolder = ChannelsHolder.getInstance();
    private UsersHolder usersHolder = UsersHolder.getInstance();
    private TempUsersHolder tempUsersHolder = TempUsersHolder.getInstance();

    @Override
    public void disconnectedFromChannel(Channel removedChannel) {
        log.info("Client disconnected");
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
//        log.info("AbstractPacket accepted opcode = " + packet.getOpCode());
        if (packet.getOpCode() >= OpCodes.ISOpCodesStart) {
            switch (packet.getOpCode()) {
                case OpCodes.InterServer.ISMSG_HELLO: {
                    log.info("Interserver " + ((HelloPacket) packet).getHello() + " sent hello to us!");
                }
                break;
                case OpCodes.InterServer.ISMSG_REG: {
                    int startRange = ((RegisterPacket) packet).getStartRange();
                    int endRange = ((RegisterPacket) packet).getEndRange();
                    log.info("Registering interserver for range " + startRange + " to " + endRange);
                    for (int i = startRange; i < endRange; i++) {
                        channelsHolder.addChannel(i, channel);
                    }
                }
                break;
                case OpCodes.InterServer.ISMSG_UNREG: {
                }
                break;
                case OpCodes.InterServer.ISMSG_BOOT_USER: {
                    log.info("Booting char with UID = " + packet.getSender());
                    Channel charChannel = channelsHolder.getChannel(OpCodes.UserOpcodeStart);
                    Channel chatChannel = channelsHolder.getChannel(OpCodes.ChatOpcodeStart);
                    if (charChannel != null) {
                        charChannel.write(packet);
                    }
                    if (chatChannel != null) {
                        chatChannel.write(packet);
                    }
                }
                break;
                case OpCodes.InterServer.ISMSG_CHAR_REG: {
                    Long oldId = (((UserRegPacket) packet).getOldId());
                    log.info("Registering character with oldid = " + oldId + " and new id = " + packet.getSender());
                    usersHolder.addUserChannel(packet.getSender(), tempUsersHolder.getTempChannel(oldId));
                    tempUsersHolder.deleteTempChannel(oldId);
                }
                break;
            }
        } else {
            if (packet.getOpCode().equals(OpCodes.Server.Login.SMSG_LOGIN_FAILED)) {
                tempUsersHolder.getTempChannel(packet.getSender()).write(packet);
            } else {
                if (packet.getSender().equals(0L)) {
                    //log.info("Sending packet " + packet.getOpCode() + " to all players");
                    usersHolder.getChannels().forEach(chan -> usersHolder.getUserChannel(chan).write(packet));
                } else {
                    usersHolder.getUserChannel(packet.getSender()).write(packet);
                }
            }
        }
    }

    @Override
    public void sendHello() {
        HelloPacket helloPacket = new HelloPacket();
        helloPacket.setHello("Hello, this is frontend!");
        channel.write(helloPacket);
    }

}
