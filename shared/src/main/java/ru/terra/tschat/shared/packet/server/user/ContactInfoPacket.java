package ru.terra.tschat.shared.packet.server.user;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.entity.UserInfo;
import ru.terra.tschat.shared.packet.AbstractPacket;
import ru.terra.tschat.shared.persistance.UserLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 22.03.15
 * Time: 20:11
 */
@Packet(opCode = OpCodes.Server.User.SMSG_CONTACT_INFO)
public class ContactInfoPacket extends AbstractPacket {
    private List<Long> contacts = new ArrayList<>();
    private UserLoader loader;

    public UserLoader getLoader() {
        return loader;
    }

    public void setLoader(UserLoader loader) {
        this.loader = loader;
    }

    public List<Long> getContacts() {
        return contacts;
    }

    public void setContacts(List<Long> contacts) {
        this.contacts = contacts;
    }

    @Override
    public void get(ChannelBuffer buffer) {

    }

    @Override
    public void send(ChannelBuffer buffer) {
        List<UserInfo> res = new ArrayList<>();
        for (UserInfo info : loader.loadUsers())
            if (contacts.contains(info.getUID()))
                res.add(info);

        buffer.writeInt(res.size());
        if (res.size() > 0)
            for (UserInfo info : res)
                writeString(buffer, info.getName());
    }
}
