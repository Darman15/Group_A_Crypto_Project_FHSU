package org.example;

import sockets.JimSender;
import java.util.Scanner;

public class Jim {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        JimSender sender = new JimSender();

        while (true) {
            System.out.print("\nEnter your message (or 'exit' to quit): ");
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("exit")) break;
            sender.send(message, 1);
        }

        System.out.println("Jim: Goodbye!");
    }
}