package sockets;

import java.net.*;
import java.io.*;

public class DwightInterceptor {
    private static final int LISTEN_PORT = 8888;
    private static final String PAM_HOST = "localhost";
    private static final int PAM_PORT = 9999;

    public void start() throws Exception {
        System.out.println("Dwight: Listening for messages...");

        try (ServerSocket serverSocket = new ServerSocket(LISTEN_PORT)) {
            while (true) {
                try (Socket jimSocket = serverSocket.accept();
                     Socket pamSocket = new Socket(PAM_HOST, PAM_PORT)) {

                    DataInputStream disJim = new DataInputStream(jimSocket.getInputStream());
                    DataOutputStream dosJim = new DataOutputStream(jimSocket.getOutputStream());
                    DataInputStream disPam = new DataInputStream(pamSocket.getInputStream());
                    DataOutputStream dosPam = new DataOutputStream(pamSocket.getOutputStream());

                    // Forward Pam's RSA public key (N, E) to Jim
                    int nLen = disPam.readInt();
                    byte[] nBytes = new byte[nLen];
                    disPam.readFully(nBytes);
                    int eLen = disPam.readInt();
                    byte[] eBytes = new byte[eLen];
                    disPam.readFully(eBytes);

                    dosJim.writeInt(nLen);
                    dosJim.write(nBytes);
                    dosJim.writeInt(eLen);
                    dosJim.write(eBytes);
                    dosJim.flush();

                    // Read Jim's message: method, encrypted key, encrypted message
                    int method = disJim.readInt();

                    int keyLength = disJim.readInt();
                    byte[] encryptedKey = new byte[keyLength];
                    disJim.readFully(encryptedKey);

                    int length = disJim.readInt();
                    byte[] messageBytes = new byte[length];
                    disJim.readFully(messageBytes);

                    System.out.println("Dwight: Intercepted method:         " + (method == 1 ? "DES" : "AES"));
                    System.out.println("Dwight: Encrypted DES key (hex):    " + bytesToHex(encryptedKey));
                    System.out.println("Dwight: Encrypted DES key (bits):   " + bytesToBits(encryptedKey));
                    System.out.println("Dwight: Intercepted message (bits):  " + bytesToBits(messageBytes));
                    System.out.println("Dwight: Intercepted message (hex):   " + bytesToHex(messageBytes));
                    System.out.println("Dwight: Translated as text:          " + new String(messageBytes, "UTF-8"));

                    // Forward everything to Pam
                    dosPam.writeInt(method);
                    dosPam.writeInt(keyLength);
                    dosPam.write(encryptedKey);
                    dosPam.writeInt(messageBytes.length);
                    dosPam.write(messageBytes);
                    dosPam.flush();
                    System.out.println("Dwight: Forwarded to Pam.\n");
                }
            }
        }
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

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}