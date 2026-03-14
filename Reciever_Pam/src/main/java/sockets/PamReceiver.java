package sockets;

import java.net.*;
import java.io.*;

public class PamReceiver {
    private static final int PORT = 9999;

    public void start() throws Exception {
        System.out.println("Pam: Waiting for message...");

        ServerSocket serverSocket = new ServerSocket(PORT);
        Socket socket = serverSocket.accept();

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        int length = dis.readInt();
        byte[] messageBytes = new byte[length];
        dis.readFully(messageBytes);

        String message = new String(messageBytes, "UTF-8");
        System.out.println("Pam: Received byte array: " + java.util.Arrays.toString(messageBytes));
        System.out.println("Pam: Received hex:        " + bytesToHex(messageBytes));
        System.out.println("Pam: Decoded message:     " + message);

        socket.close();
        serverSocket.close();
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