package com.example.gatewayController.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utility {
    final static String superSecret = "cFtgydw5432899yGTrfdeoiyT579Yfrde";
    public static String generateHash(String value){
        String sha3Hex;
        try {
            final MessageDigest digest = MessageDigest.getInstance("SHA3-256");
            String newString = value + superSecret;
            final byte[] hashBytes = digest.digest(newString.getBytes(StandardCharsets.UTF_8));
            sha3Hex = bytesToHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return sha3Hex;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
