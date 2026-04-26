package org.encrypt;
import java.math.BigInteger;
import java.util.Random;

public class RSA {

    //Takes string and converts characters to ASCII decimal
    //Returns BigInteger
    public static BigInteger stringCipher(String key) {
        key = key.toUpperCase();
        String cipherString = "";
        int i = 0;

        while (i < key.length()) {
            int ch = (int) key.charAt(i);
            cipherString = cipherString + ch;
            i++;
        }

        BigInteger cipherBig = new BigInteger(String.valueOf(cipherString));
        return cipherBig;
    }

    //Takes ciphered BigInt and converts to plain text
    //returns String
    public static String cipherToString(BigInteger key) {
        String cipherString = key.toString();
        String output = "";
        int i = 0;
        while (i < cipherString.length()) {
            int temp = Integer.parseInt(cipherString.substring(i, i + 2));
            char ch = (char) temp;
            output = output + ch;
            i = i + 2;
        }
        return output;
    }

    //Compute Phi(n) Euler's totient
    public static BigInteger getPhi(BigInteger p, BigInteger q) {
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        return phi;
    }

    //Generate random large prime number at specified bitlength
    public static BigInteger largePrime(int bits) {
        Random randomInteger = new Random();
        BigInteger largePrime = BigInteger.probablePrime(bits, randomInteger);
        return largePrime;
    }

    //Recursive Euclidean algo to find gcd
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) {
            return a;
        } else {
            return gcd(b, a.mod(b));
        }
    }

    /*Recursive extended Euclidean algo to find
    multiplicative inverse
    returns d, p, q; d = gcd(a,b) and ap + pq = d */
    public static BigInteger[] extEuclid(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) return new BigInteger[] {
            a, BigInteger.ONE, BigInteger.ZERO
        };
        BigInteger[] vals = extEuclid(b, a.mod(b));
        BigInteger d = vals[0];
        BigInteger p = vals [2];
        BigInteger q = vals [1].subtract(a.divide(b).multiply(vals[2]));
        return new BigInteger[] {
            d, p, q
        };
    }

    //generate e by finding a Phi that is a coprime gcd = 1
    public static BigInteger genE(BigInteger phi) {
        Random rand = new Random();
        BigInteger e = new BigInteger(1024, rand);
        do {
            e = new BigInteger(1024, rand);
            while (e.min(phi).equals(phi)) {
                e = new BigInteger(1024, rand);
            }
        } while (!gcd(e, phi).equals(BigInteger.ONE));
        return e;
    }

    public static BigInteger encrypt(BigInteger key, BigInteger e, BigInteger n) {
        return key.modPow(e, n);
    }

    public static BigInteger decrypt(BigInteger key, BigInteger d, BigInteger n) {
        return key.modPow(d, n);
    }

    public static BigInteger n(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }
}
