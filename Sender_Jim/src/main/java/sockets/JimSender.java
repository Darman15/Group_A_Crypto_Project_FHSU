package sockets;

import java.net.*;
import java.io.*;

public class JimSender {
    private static final String HOST = "localhost";
    private static final int PORT = 9999;

    public void send(String message) throws Exception {
        Socket socket = new Socket(HOST, PORT);

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(message);

        System.out.println("Jim: Message sent: " + message);

        socket.close();
    }
}
