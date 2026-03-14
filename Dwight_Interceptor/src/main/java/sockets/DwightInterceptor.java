package sockets;

import java.net.*;
import java.io.*;

public class DwightInterceptor {
    private static final int LISTEN_PORT = 8888;
    private static final String PAM_HOST = "localhost";
    private static final int PAM_PORT = 9999;

    public void start() throws Exception {
        System.out.println("Dwight: Listening for messages...");

        ServerSocket serverSocket = new ServerSocket(LISTEN_PORT);
        Socket jimSocket = serverSocket.accept();

        DataInputStream dis = new DataInputStream(jimSocket.getInputStream());
        int length = dis.readInt();
        byte[] messageBytes = new byte[length];
        dis.readFully(messageBytes);

        System.out.println("Dwight: Intercepted bit array:  " + bytesToBits(messageBytes));
        System.out.println("Dwight: Intercepted byte array: " + java.util.Arrays.toString(messageBytes));
        System.out.println("Dwight: Intercepted hex:        " + bytesToHex(messageBytes));

        // Forward to Pam
        Socket pamSocket = new Socket(PAM_HOST, PAM_PORT);
        DataOutputStream dos = new DataOutputStream(pamSocket.getOutputStream());
        dos.writeInt(messageBytes.length);
        dos.write(messageBytes);
        System.out.println("Dwight: Forwarded to Pam.");

        jimSocket.close();
        pamSocket.close();
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