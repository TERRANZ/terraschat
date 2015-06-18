package ru.terra.tschat.shared.packet.server.user;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 22.03.15
 * Time: 19:59
 */
@Packet(opCode = OpCodes.Server.User.SMSG_USER_CONTACTS)
public class UserContactsPacket extends AbstractPacket {
    private List<Long> contacts = new ArrayList<>();

    public List<Long> getContacts() {
        return contacts;
    }

    public void setContacts(List<Long> contacts) {
        this.contacts = contacts;
    }

    @Override
    public void onRead(ChannelBuffer buffer) {
        int count = buffer.readInt();
        for (int i = 0; i < count; i++)
            contacts.add(buffer.readLong());

    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        buffer.writeInt(contacts.size());
        if (contacts.size() > 0)
            for (Long c : contacts)
                buffer.writeLong(c);
    }
}
