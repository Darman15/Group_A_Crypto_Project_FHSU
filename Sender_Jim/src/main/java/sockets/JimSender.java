package sockets;

import java.net.*;
import java.io.*;

public class JimSender {
    private static final String HOST = "localhost";
    private static final int PORT = 8888;

    public void send(String message) throws Exception {
        Socket socket = new Socket(HOST, PORT);

        // Convert message to byte array
        byte[] messageBytes = message.getBytes("UTF-8");

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeInt(messageBytes.length);
        dos.write(messageBytes);

        System.out.println("Jim: Message as bit array:  " + bytesToBits(messageBytes));
        System.out.println("Jim: Message as byte array: " + java.util.Arrays.toString(messageBytes));
        System.out.println("Jim: Message as hex:        " + bytesToHex(messageBytes));

        socket.close();
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }

    public static String bytesToBits(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
            sb.append(" ");
        }
        return sb.toString().trim();
    }
}