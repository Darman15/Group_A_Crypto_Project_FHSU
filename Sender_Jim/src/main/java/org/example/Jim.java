package org.example;

import sockets.JimSender;
import java.util.Scanner;

public class Jim {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select encryption method:");
        System.out.println("1 = DES");
        System.out.println("2 = AES (coming soon)");
        System.out.print("Choice: ");
        int method = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter your message: ");
        String message = scanner.nextLine();

        JimSender sender = new JimSender();
        sender.send(message, method);
    }
}