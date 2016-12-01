package ru.terra.tschat.server.frontend.server;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.Channel;
import ru.terra.tschat.server.frontend.network.netty.ServerWorker;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.packet.interserver.PingPacket;
import ru.terra.tschat.shared.packet.interserver.UnregCharPacket;
import ru.terra.tschat.shared.packet.server.OkPacket;

import java.util.UUID;

public class FrontEndServerWorker extends ServerWorker {

    private Logger logger = Logger.getLogger(this.getClass());
    private UsersHolder usersHolder = UsersHolder.getInstance();
    private ChannelsHolder channelsHolder = ChannelsHolder.getInstance();

    @Override
    public void disconnectedFromChannel(Channel removedChannel) {
        logger.info("User disconnected");
        Long removedChar = usersHolder.deleteUserChannel(removedChannel);
        if (removedChar != null) {
            Channel userChannel = channelsHolder.getChannel(OpCodes.UserOpcodeStart);
            Channel chatChannel = channelsHolder.getChannel(OpCodes.ChatOpcodeStart);
            Channel loginChannel = channelsHolder.getChannel(OpCodes.LoginOpcodeStart);
            UnregCharPacket unregCharPacket = new UnregCharPacket();
            unregCharPacket.setSender(removedChar);
            if (userChannel != null)
                userChannel.write(unregCharPacket);
            if (chatChannel != null)
                chatChannel.write(unregCharPacket);
            if (loginChannel != null)
                loginChannel.write(unregCharPacket);
        }
    }

    @Override
    public void acceptPacket(AbstractPacket packet) {
//        logger.info("Received packet " + packet.getOpCode());
        Channel interchan = channelsHolder.getChannel(packet.getOpCode());
        if (interchan != null)
            try {
                interchan.write(packet);
            } catch (Exception e) {
                logger.error("Channel closed", e);
            }
        else {
            if (packet.getOpCode() == OpCodes.InterServer.ISMSG_PING) {
                logger.info("Received ping: " + ((PingPacket) packet).getTs());
            } else
                logger.error("Unable to find interserver for opcode " + packet.getOpCode());
        }
    }

    @Override
    synchronized public void sendHello() {
        logger.info("User connected");
        OkPacket okPacket = new OkPacket();
        okPacket.setSender(UUID.randomUUID().getLeastSignificantBits());
        logger.info("Temporary player uid = " + okPacket.getSender() + " with channel " + channel.getId());
        usersHolder.addUserChannel(okPacket.getSender(), channel);
        channel.write(okPacket);
    }
}
