package org.example;

import sockets.PamReceiver;

public class Pam {
    public static void main(String[] args) throws Exception {
        PamReceiver receiver = new PamReceiver();
        receiver.start();
    }
}