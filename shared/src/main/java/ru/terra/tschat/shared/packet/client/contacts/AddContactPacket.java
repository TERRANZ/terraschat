package ru.terra.tschat.shared.packet.client.contacts;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * Date: 24.03.15
 * Time: 11:17
 */
@Packet(opCode = OpCodes.Client.User.CMSG_ADD_CONTACT)
public class AddContactPacket extends AbstractPacket {
    private String contactUID = "";

    public String getContactUID() {
        return contactUID;
    }

    public void setContactUID(String contactUID) {
        this.contactUID = contactUID;
    }

    @Override
    public void onRead(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) {
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        writeString(buffer, contactUID);
    }
}
