package org.example;

public class DES {
    // Initial Permutation Table (This is just using what out book uses for now, Can change it later.
    public static final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17,  9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    // Final Permutation Table (inverse of IP)
    public static final int[] FP = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41,  9, 49, 17, 57, 25
    };


    // Parity Drop Table (64-bit key -> 56-bit key)
    private static final int[] PC1 = {
            57, 49, 41, 33, 25, 17,  9,
            1, 58, 50, 42, 34, 26, 18,
            10,  2, 59, 51, 43, 35, 27,
            19, 11,  3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14,  6, 61, 53, 45, 37, 29,
            21, 13,  5, 28, 20, 12,  4
    };

    // Compression Permutation Table (56-bit -> 48-bit)
    private static final int[] PC2 = {
            14, 17, 11, 24,  1,  5,
            3, 28, 15,  6, 21, 10,
            23, 19, 12,  4, 26,  8,
            16,  7, 27, 20, 13,  2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };



    public static int[] permute(int[] bits, int[] table) {
        int[] output = new int[table.length];
        for (int i = 0; i < table.length; i++) {
            output[i] = bits[table[i] - 1];
        }
        return output;
    }


    public static int[] bytesToBits(byte[] bytes) {
        int[] bits = new int[bytes.length * 8];
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < 8; j++) {
                bits[i * 8 + j] = (bytes[i] >> (7 - j)) & 1;
            }
        }
        return bits;
    }


    public static byte[] bitsToBytes(int[] bits) {
        byte[] bytes = new byte[bits.length / 8];
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < 8; j++) {
                bytes[i] = (byte) ((bytes[i] << 1) | bits[i * 8 + j]);
            }
        }
        return bytes;
    }


    public static void printBits(String label, int[] bits) {
        StringBuilder sb = new StringBuilder(label + ": ");
        for (int i = 0; i < bits.length; i++) {
            if (i > 0 && i % 8 == 0) sb.append(" ");
            sb.append(bits[i]);
        }
        System.out.println(sb.toString());
    }

    // Number of left shifts per round
    private static final int[] SHIFT_SCHEDULE = {
            1, 1, 2, 2, 2, 2, 2, 2,
            1, 2, 2, 2, 2, 2, 2, 1
    };

    // Generate 16 subkeys from original key
    public static int[][] generateSubkeys(byte[] keyBytes) {
        // Step 1: Convert key to bits and apply PC1 (64 -> 56 bits)
        int[] keyBits = bytesToBits(keyBytes);
        int[] permutedKey = permute(keyBits, PC1);

        // Step 2: Split into two 28-bit halves C and D
        int[] C = java.util.Arrays.copyOfRange(permutedKey, 0, 28);
        int[] D = java.util.Arrays.copyOfRange(permutedKey, 28, 56);

        // Step 3: Generate 16 subkeys
        int[][] subkeys = new int[16][48];

        for (int round = 0; round < 16; round++) {
            // Left shift both halves
            C = leftShift(C, SHIFT_SCHEDULE[round]);
            D = leftShift(D, SHIFT_SCHEDULE[round]);

            // Combine C and D
            int[] CD = new int[56];
            System.arraycopy(C, 0, CD, 0, 28);
            System.arraycopy(D, 0, CD, 28, 28);

            // Apply PC2 to get 48-bit subkey
            subkeys[round] = permute(CD, PC2);

            printBits("Subkey " + (round + 1), subkeys[round]);
        }

        return subkeys;
    }

    // Left shift a bit array by n positions
    private static int[] leftShift(int[] bits, int n) {
        int[] shifted = new int[bits.length];
        for (int i = 0; i < bits.length; i++) {
            shifted[i] = bits[(i + n) % bits.length];
        }
        return shifted;
    }

    // Convert bit array to hex string for display
    public static String bitsToHex(int[] bits) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bits.length; i += 4) {
            int nibble = bits[i] * 8 + bits[i+1] * 4 + bits[i+2] * 2 + bits[i+3];
            sb.append(String.format("%X", nibble));
        }
        return sb.toString();
    }

}
