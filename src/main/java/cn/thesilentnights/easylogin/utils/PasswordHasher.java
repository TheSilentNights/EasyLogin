package cn.thesilentnights.easylogin.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHasher {

        private static final String ALGORITHM = "SHA-256";
        private static final int SALT_LENGTH = 16;

        public static String hash(String password) {
                byte[] salt = generateSalt();
                byte[] hash = hash(password, salt);
                return encodeSalt(salt) + "$" + encodeHash(hash);
        }

        public static boolean verify(String password, String storedHash) {
                String[] parts = storedHash.split("\\$");
                if (parts.length != 2) {
                        return false;
                }
                byte[] salt = decodeSalt(parts[0]);
                byte[] expectedHash = decodeHash(parts[1]);
                byte[] actualHash = hash(password, salt);
                return MessageDigest.isEqual(expectedHash, actualHash);
        }

        private static byte[] generateSalt() {
                SecureRandom random = new SecureRandom();
                byte[] salt = new byte[SALT_LENGTH];
                random.nextBytes(salt);
                return salt;
        }

        private static byte[] hash(String password, byte[] salt) {
                try {
                        MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
                        digest.update(salt);
                        return digest.digest(password.getBytes(StandardCharsets.UTF_8));
                } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException("SHA-256 algorithm not available", e);
                }
        }

        private static String encodeSalt(byte[] salt) {
                return Base64.getEncoder().encodeToString(salt);
        }

        private static String encodeHash(byte[] hash) {
                return Base64.getEncoder().encodeToString(hash);
        }

        private static byte[] decodeSalt(String encodedSalt) {
                return Base64.getDecoder().decode(encodedSalt);
        }

        private static byte[] decodeHash(String encodedHash) {
                return Base64.getDecoder().decode(encodedHash);
        }
}
