package com.dailydatahub.dailydatacrawler.module;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AESCompoent {
    private String ALGORITHM = "AES";
    private String KEY = "dailydatacrawler"; // Replace with your own secret key

    public String encryptApiKey(String apiKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encrypted = cipher.doFinal(apiKey.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public String decryptApiKey(String encryptedApiKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedApiKey));
        return new String(decrypted);
    }
}
