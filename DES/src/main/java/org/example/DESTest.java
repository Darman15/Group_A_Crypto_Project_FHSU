package org.example;

public class DESTest {
    public static void main(String[] args) {
        // "Hi" in bytes = [72, 105] but DES needs 64 bits so pad to 8 bytes
        byte[] input = {72, 105, 32, 80, 97, 109, 33, 33}; // "Hi Pam!!"

        // Convert to bits
        int[] bits = DES.bytesToBits(input);
        DES.printBits("Original ", bits);

        // Apply Initial Permutation
        int[] ipResult = DES.permute(bits, DES.IP);
        DES.printBits("After IP ", ipResult);

        // Apply Final Permutation (should restore original)
        int[] fpResult = DES.permute(ipResult, DES.FP);
        DES.printBits("After FP ", fpResult);

        // Verify they match
        boolean match = java.util.Arrays.equals(bits, fpResult);
        System.out.println("IP -> FP restores original: " + match);
    }
}