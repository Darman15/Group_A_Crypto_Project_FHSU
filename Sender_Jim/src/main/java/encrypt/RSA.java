package encrypt;
import java.math.BigInteger;
import java.util.Random;

public class RSA {

    // RSA key pair generated once when the class is loaded.
    // P and Q are secret large primes; N is the public modulus (P*Q).
    // PHI is Euler's totient (P-1)*(Q-1), used only during key generation.
    // E is the public exponent; D is the private exponent (E's modular inverse mod PHI).
    protected static final BigInteger P;
    protected static final BigInteger Q;
    protected static final BigInteger N;
    protected static final BigInteger PHI;
    protected static final BigInteger E;
    protected static final BigInteger D;

    static {
        P   = largePrime(1024);
        Q   = largePrime(1024);
        N   = computeModulus(P, Q);
        PHI = getPhi(P, Q);
        E   = genE(PHI);
        D   = E.modInverse(PHI);
    }

    // Converts a hex string (e.g. "1A2B") to its ASCII character representation.
    public static String hexToString(String hex) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hex.length(); i += 2) {
            String str = hex.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString().trim();
    }

    // Converts a string to a BigInteger by concatenating the ASCII decimal value
    // of each character (e.g. "AB" -> 6566).
    public static BigInteger stringCipher(String key) {
        key = key.toUpperCase();
        StringBuilder cipherString = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            cipherString.append((int) key.charAt(i));
        }
        return new BigInteger(cipherString.toString());
    }

    // Reverses stringCipher: splits the numeric string into 2-digit chunks
    // and converts each back to its ASCII character.
    public static String cipherToString(BigInteger key) {
        String cipherString = key.toString();
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < cipherString.length(); i += 2) {
            int temp = Integer.parseInt(cipherString.substring(i, i + 2));
            output.append((char) temp);
        }
        return output.toString();
    }

    // Computes Euler's totient: PHI(N) = (P-1)*(Q-1).
    // Measures how many integers less than N are coprime to N.
    public static BigInteger getPhi(BigInteger prime1, BigInteger prime2) {
        return prime1.subtract(BigInteger.ONE).multiply(prime2.subtract(BigInteger.ONE));
    }

    // Generates a random probable prime of the given bit length.
    public static BigInteger largePrime(int bits) {
        return BigInteger.probablePrime(bits, new Random());
    }

    // Returns the greatest common divisor of a and b.
    public static BigInteger gcd(BigInteger a, BigInteger b) {
        return a.gcd(b);
    }

    // Extended Euclidean algorithm: returns {gcd, x, y} such that a*x + b*y = gcd(a,b).
    // Used to find modular inverses.
    public static BigInteger[] extEuclid(BigInteger a, BigInteger b) {
        if (b.equals(BigInteger.ZERO)) return new BigInteger[]{a, BigInteger.ONE, BigInteger.ZERO};
        BigInteger[] vals = extEuclid(b, a.mod(b));
        BigInteger gcd = vals[0];
        BigInteger x   = vals[2];
        BigInteger y   = vals[1].subtract(a.divide(b).multiply(vals[2]));
        return new BigInteger[]{gcd, x, y};
    }

    // Generates the public exponent E: a random number smaller than PHI
    // that shares no common factors with PHI (gcd(E, PHI) == 1).
    public static BigInteger genE(BigInteger totient) {
        Random rand = new Random();
        BigInteger candidate;
        do {
            candidate = new BigInteger(totient.bitLength() - 1, rand);
        } while (candidate.compareTo(BigInteger.ONE) <= 0 || !candidate.gcd(totient).equals(BigInteger.ONE));
        return candidate;
    }

    // RSA encryption: cipher = key^pubE mod modulus.
    // Used by Jim to encrypt the DES key with Pam's public key (E, N).
    public static BigInteger encryptKey(BigInteger key, BigInteger pubE, BigInteger modulus) {
        return key.modPow(pubE, modulus);
    }

    // Computes the RSA modulus N = P * Q.
    public static BigInteger computeModulus(BigInteger prime1, BigInteger prime2) {
        return prime1.multiply(prime2);
    }

    // converts byte array to a BigInteger and encrypts
    // using this class's own key pair (E, N).
    public static BigInteger encrypt(byte[] bytes) {
        BigInteger cipherKey = new BigInteger(1, bytes);
        return encryptKey(cipherKey, E, N);
    }

    // RSA decryption using this class's own key pair: plain = cipher^D mod N.
    public static byte[] decrypt(BigInteger cipher) {
        return cipher.modPow(D, N).toByteArray();
    }
}
