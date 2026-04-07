package org.example.encrypt;

public class DESTest {
    public static void main(String[] args) throws Exception {
        byte[] input = {72, 105, 32, 80, 97, 109, 33, 33}; // "Hi Pam!!"

        byte[] key = {
                (byte)0x13, (byte)0x34, (byte)0x57,
                (byte)0x79, (byte)0x9B, (byte)0xBC,
                (byte)0xDF, (byte)0xF1
        };

        // --- Quiet Mode ---
        System.out.println("=== Quiet Mode ===");
        DES.verbose = false;
        byte[] encrypted = DES.encrypt(input, key);
        byte[] decrypted = DES.decrypt(encrypted, key);
        System.out.println("Original:  " + new String(input));
        System.out.println("Encrypted: " + DES.bitsToHex(DES.bytesToBits(encrypted)));
        System.out.println("Decrypted: " + new String(decrypted));
        System.out.println("Match:     " + java.util.Arrays.equals(input, decrypted));

        // --- Verbose Mode (single encrypt only) ---
        System.out.println("\n=== Verbose Mode ===");
        DES.verbose = true;
        byte[] verboseEncrypted = DES.encrypt(input, key);
        System.out.println("\nFinal encrypted (hex): " + DES.bitsToHex(DES.bytesToBits(verboseEncrypted)));

        // Reset after verbose
        DES.verbose = false;

        // Padding test
        byte[] msg = "Hello".getBytes();
        byte[] padded = DESPadding.pad(msg);
        byte[] unpadded = DESPadding.unpad(padded);
        System.out.println("Padded length:   " + padded.length);   // should be 8
        System.out.println("Unpadded length: " + unpadded.length); // should be 5
        System.out.println("Unpadded text:   " + new String(unpadded)); // should be Hello

    }
}