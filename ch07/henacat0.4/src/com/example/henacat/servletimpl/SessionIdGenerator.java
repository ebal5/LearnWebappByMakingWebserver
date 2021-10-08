package com.example.henacat.servletimpl;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SessionIdGenerator {
    private SecureRandom random;

    public String generateSessionId() {
        var bytes = new byte[16];
        this.random.nextBytes(bytes);
        var buffer = new StringBuilder();

        for (var i = 0; i < bytes.length; i++) {
            buffer.append(Integer.toHexString((int) (bytes[i] & 0xff)).toUpperCase());
        }
        return buffer.toString();
    }

    public SessionIdGenerator() {
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException ex) {
            System.out.println(ex);
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
