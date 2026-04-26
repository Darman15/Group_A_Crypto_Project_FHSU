package org.encrypt;
import java.awt.EventQueue;
import java.io.*;
import java.util.ArrayList;
import java.math.BigInteger;

public class RSAtest {
    public static void main(String[] args) {
        BigInteger p = largePrime(512);
        BigInteger q = largePrime(512);
        BigInteger n = n(p, q);
        BigInteger phi = getPhi(p, q);
        BigInteger e = genE(phi);
        BigInteger d = extEuclid(e, phi)[1];

        System.out.println("p: " + p);
        System.out.println("q: " + q);
		System.out.println("n: " + n);
		System.out.println("Phi: " + phi);
		System.out.println("e: " + e);
		System.out.println("d: " + d);

        String key = "Key Test";
        BigInteger cipherKey = stringCipher(key);
        BigInteger encryptKey = encrypt(cipherKey, e, n);
        BigInteger decryptKey = decrypt(encryptKey, d, n);
        String restoredKey = cipherToString(decryptKey);

        System.out.println(key);
        System.out.println(cipherKey);
        System.out.println(encryptKey);
        System.out.println(decryptKey);
        System.out.println(restoredKey);
    }
    
}
