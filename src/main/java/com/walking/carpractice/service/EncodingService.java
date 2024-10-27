package com.walking.carpractice.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncodingService {

    private final MessageDigest messageDigest = getMessageDigest();

    public String encode(String value) {
        return new String(messageDigest.digest(value.getBytes()));
    }

    public boolean match(String value, String encodedValue) {
        return encodedValue.equals(encode(value));
    }

    private MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
