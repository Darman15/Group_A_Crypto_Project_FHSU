package encrypt;

public class RSAPadding {

    public static byte[] pad(byte[] data, int blockSize) {
        int paddingLength = blockSize - (data.length % blockSize);
        byte[] padded = new byte[data.length + paddingLength];
        System.arraycopy(data, 0, padded, 0, data.length);
        for (int i = data.length; i < padded.length; i++) {
            padded[i] = (byte) paddingLength;
        }
        return padded;
    }

    public static byte[] unpad(byte[] padded) {
        int paddingLength = padded[padded.length - 1] & 0xFF;
        if (paddingLength <= 0 || paddingLength > padded.length) {
            throw new IllegalArgumentException("Invalid padding");
        }
        byte[] unpadded = new byte[padded.length - paddingLength];
        System.arraycopy(padded, 0, unpadded, 0, unpadded.length);
        return unpadded;
    }
}
