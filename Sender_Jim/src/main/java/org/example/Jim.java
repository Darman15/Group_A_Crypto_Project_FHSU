package org.example;

import sockets.JimSender;
import java.util.Scanner;

public class Jim {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("enter your message: ");
        String message = scanner.nextLine();

        JimSender sender = new JimSender();
        sender.send(message);
    }
}