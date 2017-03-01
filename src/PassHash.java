import java.security.SecureRandom;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.xml.bind.DatatypeConverter;

// This implementation was heavily inspired by https://github.com/defuse/password-hashing/blob/master/PasswordStorage.java
// It is a standard implementation of PBKDF2 with SHA1 as the underlying hash function

public class PassHash {

    // Main method is for testing purposes
    // public static void main(String[] args) throws CannotPerformOperationException, InvalidHashException {
    //     if (args.length == 2) {
    //         try {
    //             String pass = args[0];
    //             String attempt = args[1];
    //             String passHash = createHash(pass);
    //             if (verifyPass(attempt, passHash)) {
    //                 System.out.println("Login verified");
    //                 System.out.println(passHash);
    //                 return;
    //             } else {
    //                 System.out.println("Username or password is incorrect");
    //                 System.out.println(passHash);
    //                 return;
    //             }
    //         } catch (CannotPerformOperationException e) {
    //             throw new CannotPerformOperationException("Couldn't create hash of password or verify password", e);
    //         } catch (InvalidHashException e) {
    //             throw new InvalidHashException("Couldn't create hash of password or verify passworkd", e);
    //         }
            
    //     } else {
    //         System.out.println("Please use a single argument");
    //         return;
    //     }
    // }
    
    @SuppressWarnings("serial")
    public static class InvalidHashException extends Exception {
        
        public InvalidHashException(String message) {
            super(message);
        }
        public InvalidHashException(String message, Throwable source) {
            super(message, source);
        }
    }
    
    @SuppressWarnings("serial")
    public static class CannotPerformOperationException extends Exception {
        public CannotPerformOperationException(String message) {
            super(message);
        }
        public CannotPerformOperationException(String message, Throwable source) {
            super(message, source);
        }
    }
    
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA1";
    
    public static final int SALT_BYTE_SIZE = 24;
    public static final int HASH_BYTE_SIZE = 18;
    public static final int PBKDF2_ITERATIONS = 64000;
    
    public static final int HASH_SECTIONS = 5;
    public static final int HASH_ALGORITHM_INDEX = 0;
    public static final int ITERATION_INDEX = 1;
    public static final int HASH_SIZE_INDEX = 2;
    public static final int SALT_INDEX = 3;
    public static final int PBKDF2_INDEX = 4;

    public static String createHash(String pass) throws CannotPerformOperationException {
        return createHash(pass.toCharArray());
    }

    public static String createHash(char[] pass) throws CannotPerformOperationException {
        // Generate salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_BYTE_SIZE];
        random.nextBytes(salt);

        // Generate hash
        byte[] hash = pbkdf2(pass, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        int hashSize = hash.length;

        //format: algorithm:iterations:hashSize:salt:hash
        String parts = "sha1:" + PBKDF2_ITERATIONS + ":" + hashSize + ":" + toBase64(salt) + ":" + toBase64(hash);
        return parts;
    }

    public static boolean verifyPass(String pass, String correctHash) throws CannotPerformOperationException, InvalidHashException {
        return verifyPass(pass.toCharArray(), correctHash);
    }

    public static boolean verifyPass(char[] pass, String correctHash) throws CannotPerformOperationException, InvalidHashException {
        // Get hash parameters
        String[] params = correctHash.split(":");
        if (params.length != HASH_SECTIONS) {
            throw new InvalidHashException("Fields are missing from the password hash");
        }

        // I think Java only supports SHA1
        if (!params[HASH_ALGORITHM_INDEX].equals("sha1")) {
            throw new CannotPerformOperationException("Unsupported hash type");
        }

        int iterations = 0;
        try {
            iterations = Integer.parseInt(params[ITERATION_INDEX]);
        } catch (NumberFormatException e) {
            throw new InvalidHashException("Could not parse the iteration count as integer", e);
        }

        if (iterations < 1) {
            throw new InvalidHashException("Invalid number of iterations. Must be >= 1");
        }

        byte[] salt = null;
        try {
            salt = fromBase64(params[SALT_INDEX]);
        } catch (IllegalArgumentException e) {
            throw new InvalidHashException("Base64 decoding of salt failed", e);
        }

        byte[] hash = null;
        try {
            hash = fromBase64(params[PBKDF2_INDEX]);
        } catch (IllegalArgumentException e) {
            throw new InvalidHashException("Base64 decoding of pbkdf2 output failed", e);
        }

        int storedHashSize = 0;
        try {
            storedHashSize = Integer.parseInt(params[HASH_SIZE_INDEX]);
        } catch (NumberFormatException e) {
            throw new InvalidHashException("Could not parse the hash size as an integer", e);
        }

        if (storedHashSize != hash.length) {
            throw new InvalidHashException("Hash length doesn't match stored hash length");
        }

        // New hash creation for comparison
        byte[] testHash = pbkdf2(pass, salt, iterations, hash.length);
        // Comparison
        return slowEquals(hash, testHash);
    }

    private static boolean slowEquals(byte[] hash, byte[] testHash) {
        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] pbkdf2(char[] pass, byte[] salt, int iterations, int bytes) throws CannotPerformOperationException {
        try {
            PBEKeySpec spec = new PBEKeySpec(pass, salt, iterations, bytes * 8);
            SecretKeyFactory skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException e) {
            throw new CannotPerformOperationException("Hash algorithm not supported", e);
        } catch (InvalidKeySpecException e) {
            throw new CannotPerformOperationException("Invalid key spec", e);
        }
    }

    private static byte[] fromBase64(String hex) throws IllegalArgumentException {
        return DatatypeConverter.parseBase64Binary(hex);
    }

    private static String toBase64(byte[] array) {
        return DatatypeConverter.printBase64Binary(array);
    }
}
