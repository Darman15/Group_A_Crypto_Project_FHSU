package sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;

import encrypt.DES;
import encrypt.DESPadding;
import encrypt.RSA;
import encrypt.RSAPadding;

public class PamReceiver {
    private static final int PORT = 9999;
    private byte[] KEY;

    

    public void start() throws Exception {
        System.out.println("=== RSA Key Pair ===");
        System.out.println("Public Key  (N): " + RSA.N);
        System.out.println("Public Key  (E): " + RSA.E);
        System.out.println("Private Key (D): " + RSA.D);
        System.out.println("====================\n");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                System.out.println("Pam: Waiting for message...");
                try (Socket socket = serverSocket.accept()) {
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                    // Send RSA public key (N, E) so Jim can encrypt the DES key for us
                    byte[] nBytes = RSA.N.toByteArray();
                    byte[] eBytes = RSA.E.toByteArray();
                    dos.writeInt(nBytes.length);
                    dos.write(nBytes);
                    dos.writeInt(eBytes.length);
                    dos.write(eBytes);
                    dos.flush();

                    // Read method choice
                    int method = dis.readInt();
                    System.out.println("Pam: Encryption method received: " + (method == 1 ? "DES" : "AES"));

                    // Read encrypted key
                    int keyLength = dis.readInt();
                    byte[] encryptedKey = new byte[keyLength];
                    dis.readFully(encryptedKey);

                    // Read encrypted bytes
                    int length = dis.readInt();
                    byte[] encrypted = new byte[length];
                    dis.readFully(encrypted);

                    System.out.println("Pam: Encrypted bits: " + bytesToBits(encrypted));
                    System.out.println("Pam: Encrypted hex:  " + DES.bitsToHex(DES.bytesToBits(encrypted)));

                    // Decrypt KEY using Pam's private key
                    byte[] rawDecrypted = RSA.decrypt(new BigInteger(1, encryptedKey), RSA.D, RSA.N).toByteArray();
                    // Reconstruct the fixed 32-byte padded block (BigInteger may strip leading zero bytes)
                    byte[] decryptedPadded = new byte[32];
                    int srcOff = Math.max(0, rawDecrypted.length - 32);
                    int dstOff = Math.max(0, 32 - rawDecrypted.length);
                    System.arraycopy(rawDecrypted, srcOff, decryptedPadded, dstOff, rawDecrypted.length - srcOff);
                    // Remove RSA padding to recover the original DES key
                    KEY = RSAPadding.unpad(decryptedPadded);

                    // Decrypt each 8-byte block
                    byte[] decrypted = decryptAllBlocks(encrypted);

                    // Remove padding
                    byte[] unpadded = DESPadding.unpad(decrypted);

                    String message = new String(unpadded, "UTF-8");
                    System.out.println("Pam: Decrypted bits: " + bytesToBits(unpadded));
                    System.out.println("Pam: Decrypted message: " + message + "\n");
                }
            }
        }
    }

    private byte[] decryptAllBlocks(byte[] encrypted) throws Exception {
        byte[] decrypted = new byte[encrypted.length];
        for (int i = 0; i < encrypted.length; i += 8) {
            byte[] block = new byte[8];
            System.arraycopy(encrypted, i, block, 0, 8);
            byte[] decryptedBlock = DES.decrypt(block, KEY);
            System.arraycopy(decryptedBlock, 0, decrypted, i, 8);
        }
        return decrypted;
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
}