package sockets;

import encrypt.DES;
import encrypt.DESPadding;

import java.io.*;
import java.net.*;

public class JimSender {
    private static final String HOST = "localhost";
    private static final int PORT = 8888;

    // Hardcoded shared key (Jim and Pam both know this)
    private static final byte[] KEY = {
            (byte)0x13, (byte)0x34, (byte)0x57,
            (byte)0x79, (byte)0x9B, (byte)0xBC,
            (byte)0xDF, (byte)0xF1
    };

    public void send(String message, int method) throws Exception {
        Socket socket = new Socket(HOST, PORT);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        // Convert message to bytes
        byte[] messageBytes = message.getBytes("UTF-8");

        // Pad to multiple of 8 bytes
        byte[] padded = DESPadding.pad(messageBytes);

        // Encrypt each 8-byte block
        byte[] encrypted = encryptAllBlocks(padded);

        // Send method choice first (1 = DES, 2 = AES coming later)
        dos.writeInt(method);

        // Send encrypted bytes
        dos.writeInt(encrypted.length);
        dos.write(encrypted);

        System.out.println("\nJim: Original message:    " + message);
        System.out.println("Jim: Original bits:       " + bytesToBits(messageBytes));
        System.out.println("Jim: Encrypted bits:      " + bytesToBits(encrypted));
        System.out.println("Jim: Encrypted hex:       " + DES.bitsToHex(DES.bytesToBits(encrypted)));
        System.out.println("Jim: Message sent!");

        socket.close();
    }

    private byte[] encryptAllBlocks(byte[] padded) throws Exception {
        byte[] encrypted = new byte[padded.length];
        for (int i = 0; i < padded.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(padded, i, block, 0, 8);
            byte[] encryptedBlock = DES.encrypt(block, KEY);
            System.arraycopy(encryptedBlock, 0, encrypted, i, 8);
        }
        return encrypted;
    }

    public static String bytesToBits(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF))
                    .replace(' ', '0'));
            sb.append(" ");
        }
        return sb.toString().trim();
    }
}