package org.encrypt;
import java.math.BigInteger;
import java.util.HexFormat;

public class RSAtest {
    public static void main(String[] args) {
        BigInteger p = RSA.largePrime(64);
        BigInteger q = RSA.largePrime(64);
        BigInteger n = RSA.n(p, q);
        BigInteger phi = RSA.getPhi(p, q);
        BigInteger e = RSA.genE(phi);
        BigInteger d = RSA.extEuclid(e, phi)[1];
        if (d.signum() == -1) d = d.add(phi);

        System.out.println("p: " + p);
        System.out.println("q: " + q);
		System.out.println("n: " + n);
		System.out.println("Phi: " + phi);
		System.out.println("e: " + e);
		System.out.println("d: " + d);

        byte[] desKey = {
            (byte)0x13, (byte)0x34, (byte)0x57,
            (byte)0x79, (byte)0x9B, (byte)0xBC,
            (byte)0xDF, (byte)0xF1
        };
        BigInteger cipherKey = new BigInteger(1, desKey);
        BigInteger encryptKey = RSA.encrypt(cipherKey, e, n);
        BigInteger decryptKey = RSA.decrypt(encryptKey, d, n);
        String restoredKey = HexFormat.of().formatHex(decryptKey.toByteArray());

        System.out.println(HexFormat.of().formatHex(desKey));
        System.out.println(cipherKey);
        System.out.println(encryptKey);
        System.out.println(decryptKey);
        System.out.println(restoredKey);

        
    }
    
}
