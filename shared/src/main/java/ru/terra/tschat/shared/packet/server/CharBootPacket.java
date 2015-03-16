package ru.terra.tschat.shared.packet.server;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.packet.AbstractPacket;

import java.util.ArrayList;
import java.util.List;

@Packet(opCode = OpCodes.Server.SMSG_CHAR_BOOT)
public class CharBootPacket extends AbstractPacket {

    private UserInfo playerInfo = new UserInfo();
    private List<String> worlds = new ArrayList<>();

    @Override
    public void get(ChannelBuffer buffer) {
        int count = buffer.readInt();
        for (int i = 0; i < count; i++)
            worlds.add(readString(buffer));
        playerInfo.readUserInfo(buffer);
    }

    @Override
    public void send(ChannelBuffer buffer) {
        buffer.writeInt(worlds.size());
        if (worlds.size() > 0)
            for (String w : worlds)
                writeString(buffer, w);
        playerInfo.writeUserInfo(buffer);
    }

    public UserInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(UserInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public List<String> getWorlds() {
        return worlds;
    }

    public void setWorlds(List<String> worlds) {
        this.worlds = worlds;
    }
}