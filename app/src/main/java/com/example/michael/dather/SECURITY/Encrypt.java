package com.example.michael.dather.SECURITY;

import android.util.Base64;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by michael on 10/06/16.
 */

public class Encrypt {

    static private String IV = "AAAAAAAAAAAAAAAA";
    static private String secrectKey;
    static private String encryptedSecrectKey;


    /******* Syncronous encryption *******/
    public Encrypt() {
        secrectKey = genSecretKeyString();
        encryptedSecrectKey = rsaEncrypt(secrectKey);

        System.out.print("ENCRYPTION KEY: ");
        System.out.println(encryptedSecrectKey);
    }

    public String getEncryptedSecret() {
        return encryptedSecrectKey;
    }

    /***** AES ******/
    public String encryptString(String plainText) {
        try {
            byte[] cipher = encrypt(plainText, secrectKey);
            return Base64.encodeToString(cipher, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private byte[] encrypt(String plainText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    private String genSecretKeyString() {
        String secretKeyString = null;

        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            SecretKeySpec sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");

            secretKeyString = Base64.encodeToString(sks.getEncoded(), Base64.DEFAULT).substring (0,16);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return secretKeyString;
    }

    private String rsaEncrypt(String str) {
        PublicKey publicKey = null;
        String encrypted = null;

        try {
            String publicString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFok0ykRoHsXZE/DZKvvDqr+tabRGNSbk7xheE\n" +
                    "EuKaA3FXsXgo5HXyNEuSammdSkwI34cEHCn9W1AKnZ5XvKRlyE3TzehUxQ3dk5Fp0CqI+bh9w9XF\n" +
                    "7I++yzuBn+4pcEyu/I5pCvNaLxkoOLOLBSP7+jY60VD2BOtkSbc3knCLDwIDAQAB";

            /**** create the keys from the Strings ****/
            byte[] publicBytes = Base64.decode(publicString, Base64.DEFAULT);
            publicKey = publicKeyFromBytes(publicBytes);

            /***** Encrypt *****/
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] cipherData = cipher.doFinal(str.getBytes());
            String encryptedStringBase64 = Base64.encodeToString(cipherData, Base64.DEFAULT);

            encrypted = encryptedStringBase64;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return encrypted;
    }

    private PublicKey publicKeyFromBytes(byte[] publicBytes) {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

}
