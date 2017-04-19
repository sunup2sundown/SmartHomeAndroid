package edu.temple.m.smarthomedroid.Handlers;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by M on 4/17/2017.
 */

public class HashingHandler {

    public String hash_pass(String pw) {
        try {
            StringBuffer hexStr = new StringBuffer();
            byte[] hash;

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            hash = digest.digest(pw.getBytes(StandardCharsets.UTF_8));
            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xFF & hash[i]);
                if (hex.length() == 1) {
                    hexStr.append('0');
                }
                hexStr.append(hex);
            }

            return hexStr.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
