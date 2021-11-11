package org.duckdns.androidghost77.gamelove.service.impl;

import lombok.SneakyThrows;
import org.duckdns.androidghost77.gamelove.service.PasswordService;
import org.springframework.stereotype.Service;

import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

@Service
public class PasswordServiceImpl implements PasswordService {

    private static final int ITERATIONS = 100000;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    @SneakyThrows
    @Override
    public byte[] hashPassword(char[] password, byte[] salt) {
        KeySpec spec = new PBEKeySpec(password, salt, ITERATIONS);
        Arrays.fill(password, (char) 0);
        Arrays.fill(salt, (byte) 0);
        SecretKeyFactory f = SecretKeyFactory.getInstance(ALGORITHM);
        return f.generateSecret(spec).getEncoded();
    }

    @Override
    public boolean checkPassword(char[] password, byte[] hash, byte[] salt) {
        byte[] hashedPassword = hashPassword(password, salt);
        boolean arraysEqual = timingEquals(hashedPassword, hash);
        Arrays.fill(hashedPassword, (byte) 0);
        Arrays.fill(hash, (byte) 0);
        return arraysEqual;
    }

    /**
     * Indicates if both byte arrays are equal
     * but uses same amount of time if they are the same or different
     * to prevent timing attacks
     */
    private static boolean timingEquals(byte[] firstBytes, byte[] secondBytes) {
        boolean result = true;
        int len = firstBytes.length;
        if (len != secondBytes.length) {
            result = false;
        }
        if (len > secondBytes.length) {
            len = secondBytes.length;
        }
        for (int i = 0; i < len; i++) {
            result &= (firstBytes[i] == secondBytes[i]);
        }
        return result;
    }
}
