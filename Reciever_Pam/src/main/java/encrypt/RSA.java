package encrypt;
import java.math.BigInteger;
import java.util.Random;

public class RSA {

    public static BigInteger p = largePrime(1024);
    public static BigInteger q = largePrime(1024);
    public static BigInteger n = n(p, q);
    public static BigInteger phi = getPhi(p, q);
    public static BigInteger e = genE(phi);
    public static BigInteger d = e.modInverse(phi);

    //Convert DES key Array to hexadecimal string
    public static String HexToString(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char)Integer.parseInt(str, 16));
        }
        return output.toString().trim();
    }

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
        return BigInteger.probablePrime(bits, new Random());
    }

    //Recursive Euclidean algo to find gcd
    public static BigInteger gcd(BigInteger a, BigInteger b) {
       return a.gcd(b);
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
        BigInteger e;
        do {
            e = new BigInteger(phi.bitLength() - 1, rand);
        } while (e.compareTo(BigInteger.ONE) <= 0 || !e.gcd(phi).equals(BigInteger.ONE));
        return e;
    }

    public static BigInteger encrypt(BigInteger key, BigInteger e, BigInteger n) {
        return key.modPow(e, n);
    }

    public static BigInteger decrypt(BigInteger cipher, BigInteger d, BigInteger n) {
        return cipher.modPow(d, n);
    }

    public static BigInteger n(BigInteger p, BigInteger q) {
        return p.multiply(q);
    }
}
