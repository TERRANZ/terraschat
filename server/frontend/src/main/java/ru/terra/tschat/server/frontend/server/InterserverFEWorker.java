package ru.terra.tschat.server.frontend.server;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import ru.terra.tschat.server.frontend.network.netty.ServerWorker;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.interserver.HelloPacket;
import ru.terra.tschat.shared.packet.interserver.PingPacket;
import ru.terra.tschat.shared.packet.interserver.RegisterPacket;
import ru.terra.tschat.shared.packet.interserver.UserRegPacket;

public class InterserverFEWorker extends ServerWorker {

    private Logger log = Logger.getLogger(InterserverFEWorker.class);
    private ChannelsHolder channelsHolder = ChannelsHolder.getInstance();
    private UsersHolder usersHolder = UsersHolder.getInstance();

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
                    Channel usersChannel = channelsHolder.getChannel(OpCodes.UserOpcodeStart);
                    Channel chatChannel = channelsHolder.getChannel(OpCodes.ChatOpcodeStart);
                    if (usersChannel != null) {
                        try {
                            usersChannel.write(packet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (chatChannel != null) {
                        try {
                            chatChannel.write(packet);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
                case OpCodes.InterServer.ISMSG_USER_REG: {
                    Long oldId = (((UserRegPacket) packet).getOldId());
                    log.info("Registering character with oldid = " + oldId + " and new id = " + packet.getSender());
                    Channel chan = usersHolder.getUserChannel(oldId);
                    usersHolder.deleteUserChannel(chan);
                    usersHolder.addUserChannel(packet.getSender(), chan);
                }
                break;
                case OpCodes.InterServer.ISMSG_PING: {
                    log.info("Received ping: " + ((PingPacket) packet).getTs());
                }
                break;
            }
        } else {
            if (packet.getOpCode().equals(OpCodes.Server.Login.SMSG_LOGIN_FAILED)) {
                log.info("User " + packet.getSender() + " failed to login");
                usersHolder.getUserChannel(packet.getSender()).write(packet);
            } else {
                if (packet.getSender().equals(0L)) {
                    //log.info("Sending packet " + packet.getOpCode() + " to all players");
                    for (Long chan : usersHolder.getChannels())
                        try {
                            Channel c = usersHolder.getUserChannel(chan);
                            if (c != null)
                                c.write(packet);
                            else
                                log.error("Channel for " + chan + " is not found");
                        } catch (Exception e) {
                            log.error("Unable to send message", e);
                        }
                } else
                    try {
                        Channel c = usersHolder.getUserChannel(packet.getSender());
                        if (c != null)
                            c.write(packet);
                        else
                            log.error("Channel for " + packet.getSender() + " is not found");
                    } catch (Exception e) {
                        log.error("Unable to send message", e);
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
