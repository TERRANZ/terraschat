package ru.terra.tschat.shared.packet;

import org.jboss.netty.buffer.ChannelBuffer;
import ru.terra.tschat.interserver.network.netty.PacketCheckpointHandler;
import ru.terra.tschat.shared.annoations.Packet;
import ru.terra.tschat.shared.context.SharedContext;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.SecureRandom;

public abstract class AbstractPacket {
    private Integer opCode = 0;
    private Long sender = 0L;

    public AbstractPacket() {
        opCode = getClass().getAnnotation(Packet.class).opCode();
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Integer getOpCode() {
        return opCode;
    }

    public void setOpCode(Integer id) {
        this.opCode = id;
    }

    public static AbstractPacket read(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) throws IOException {
        Integer opcode = buffer.readUnsignedShort();
        checkpointHandler.onCheckpoint();
        Long sguid = buffer.readLong();
        checkpointHandler.onCheckpoint();
        AbstractPacket packet = PacketFactory.getInstance().getPacket(opcode, sguid);
        if (packet != null)
            packet.onRead(buffer, checkpointHandler);
        else
            SharedContext.getInstance().getLogger().error("AbstractPacket", "Unable to find packet " + opcode + " with sender guid: " + sguid);
        return packet;
    }

    public static AbstractPacket write(AbstractPacket packet, ChannelBuffer buffer) {
        buffer.writeChar(packet.getOpCode());
        buffer.writeLong(packet.getSender());
        packet.onSend(buffer);
        return packet;
    }

    // Функции, которые должен реализовать каждый класс пакета

    /**
     * Вызывается при приёме пакета, в этом методе производим вычитывание из буфера данных
     */
    public abstract void onRead(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler);

    /**
     * Вызывается при отсылке пакета, в этом методе производим запись в буфер данных
     */
    public abstract void onSend(ChannelBuffer buffer);

    public static String readString(ChannelBuffer buffer, PacketCheckpointHandler checkpointHandler) {
        int length = buffer.readShort();
        checkpointHandler.onCheckpoint();
        byte[] buf = new byte[length];
        buffer.readBytes(buf);
        String text = "";
        try {
            byte[] keyStart = "this is a key".getBytes();
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(keyStart);
            kgen.init(128, sr); // 192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            byte[] key = skey.getEncoded();
            byte[] decryptedData = decrypt(key, buf);
            text = new String(decryptedData, Charset.forName("UTF-8"));
        } catch (Exception e) {
            SharedContext.getInstance().getLogger().error("AbstractPacket", "Unable to decode string", e);
        }
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < length; ++i)
//            builder.append(buffer.readChar());
//        SharedContext.getInstance().getLogger().debug("AbstractPacket", "Readed from buffer: '" + builder.toString() + "'");
//        return builder.toString();
        return text;
    }

    public static void writeString(ChannelBuffer buffer, String text) {
        if (text == null || text.length() == 0)
            return;
        try {
            byte[] keyStart = "this is a key".getBytes();
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(keyStart);
            kgen.init(128, sr); // 192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            byte[] key = skey.getEncoded();
            byte[] encryptedData = encrypt(key, text.getBytes(Charset.forName("UTF-8")));
            buffer.writeShort(encryptedData.length);
            buffer.writeBytes(encryptedData);
        } catch (Exception e) {
            SharedContext.getInstance().getLogger().error("AbstractPacket", "Unable to encode and send string " + text, e);
        }

    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }
}
