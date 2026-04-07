package encrypt;

public class DESPadding {

    public static byte[] pad(byte[] input) {
        int paddingNeeded = 8 - (input.length % 8);
        byte[] padded = new byte[input.length + paddingNeeded];
        System.arraycopy(input, 0, padded, 0, input.length);
        for (int i = input.length; i < padded.length; i++) {
            padded[i] = (byte) paddingNeeded;
        }
        return padded;
    }

    // Remove padding after decryption
    public static byte[] unpad(byte[] input) {
        int paddingAmount = input[input.length - 1];
        byte[] unpadded = new byte[input.length - paddingAmount];
        System.arraycopy(input, 0, unpadded, 0, unpadded.length);
        return unpadded;
    }

}
