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
        String message = dis.readUTF();

        System.out.println("Pam: Message received: " + message);

        socket.close();
        serverSocket.close();
    }
}