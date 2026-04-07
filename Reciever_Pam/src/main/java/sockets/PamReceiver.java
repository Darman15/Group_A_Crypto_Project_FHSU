package sockets;

import encrypt.DES;
import encrypt.DESPadding;

import java.io.*;
import java.net.*;

public class PamReceiver {
    private static final int PORT = 9999;

    // Same shared key as Jim
    private static final byte[] KEY = {
            (byte)0x13, (byte)0x34, (byte)0x57,
            (byte)0x79, (byte)0x9B, (byte)0xBC,
            (byte)0xDF, (byte)0xF1
    };

    public void start() throws Exception {
        System.out.println("Pam: Waiting for message...");

        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = serverSocket.accept();
        DataInputStream dis = new DataInputStream(socket.getInputStream());

        // Read method choice
        int method = dis.readInt();
        System.out.println("Pam: Encryption method received: " + (method == 1 ? "DES" : "AES"));

        // Read encrypted bytes
        int length = dis.readInt();
        byte[] encrypted = new byte[length];
        dis.readFully(encrypted);

        System.out.println("Pam: Encrypted bits: " + bytesToBits(encrypted));
        System.out.println("Pam: Encrypted hex:  " + DES.bitsToHex(DES.bytesToBits(encrypted)));

        // Decrypt each 8-byte block
        byte[] decrypted = decryptAllBlocks(encrypted);

        // Remove padding
        byte[] unpadded = DESPadding.unpad(decrypted);

        String message = new String(unpadded, "UTF-8");
        System.out.println("Pam: Decrypted bits: " + bytesToBits(unpadded));
        System.out.println("Pam: Decrypted message: " + message);

        socket.close();
        serverSocket.close();
    }

    private byte[] decryptAllBlocks(byte[] encrypted) throws Exception {
        byte[] decrypted = new byte[encrypted.length];
        for (int i = 0; i < encrypted.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(encrypted, i, block, 0, 8);
            byte[] decryptedBlock = DES.decrypt(block, KEY);
            System.arraycopy(decryptedBlock, 0, decrypted, i, 8);
        }
        return decrypted;
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