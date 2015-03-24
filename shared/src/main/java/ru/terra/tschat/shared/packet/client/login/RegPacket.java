package ru.terra.tschat.shared.packet.client.login;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.constants.OpCodes;
import ru.terra.tschat.shared.packet.AbstractPacket;

/**
 * Date: 20.03.15
 * Time: 15:25
 */
@Packet(opCode = OpCodes.Client.Login.CMSG_REG)
public class RegPacket extends AbstractPacket {
    private String login = "", password = "", mail = "", phone = "";

    @Override
    public void onRead(ChannelBuffer buffer) {
        login = readString(buffer);
        password = readString(buffer);
        mail = readString(buffer);
        phone = readString(buffer);
    }

    @Override
    public void onSend(ChannelBuffer buffer) {
        writeString(buffer, login);
        writeString(buffer, password);
        writeString(buffer, mail);
        writeString(buffer, phone);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
