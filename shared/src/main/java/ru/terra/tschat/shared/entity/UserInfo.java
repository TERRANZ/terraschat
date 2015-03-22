package ru.terra.tschat.shared.entity;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.packet.AbstractPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 16.03.15
 * Time: 18:58
 */
public class UserInfo {
    protected Long UID = 0L;
    protected String name = "";
    protected List<Long> contacts = new ArrayList<>();

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

    public List<Long> getContacts() {
        return contacts;
    }

    public void setContacts(List<Long> contacts) {
        this.contacts = contacts;
    }

    public void writeUserInfo(ChannelBuffer buffer) {
        buffer.writeLong(UID);
        AbstractPacket.writeString(buffer, name);
        buffer.writeInt(contacts.size());
        for (Long c : contacts)
            buffer.writeLong(c);
    }

    public void readUserInfo(ChannelBuffer buffer) {
        UID = buffer.readLong();
        name = AbstractPacket.readString(buffer);
        for (int i = 0; i < buffer.readInt(); i++)
            contacts.add(buffer.readLong());
    }
}
