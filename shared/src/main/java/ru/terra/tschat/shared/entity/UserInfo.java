package ru.terra.tschat.shared.entity;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * Date: 16.03.15
 * Time: 18:58
 */
public class UserInfo {
    protected Long UID = 0L;
    protected String name = "";

    public Long getUID() {
        return UID;
    }

    public void setUID(Long UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void writeUserInfo(ChannelBuffer buffer) {
        buffer.writeLong(UID);
        AbstractPacket.writeString(buffer, name);
    }

    public void readUserInfo(ChannelBuffer buffer) {
        UID = buffer.readLong();
        name = AbstractPacket.readString(buffer);
    }
}
