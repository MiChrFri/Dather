package com.example.michael.dather.SECURITY;

import android.util.Base64;
import android.util.Log;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by michael on 10/06/16.
 */

public class Encrypt {
    private SecretKeySpec sks = null;
    static String IV = "AAAAAAAAAAAAAAAA";

    private String secretKeyString;


    /******* Syncronous encryption *******/
    public Encrypt() {
        String encryptionKey = generateKeyString();


        System.out.print("ENCRYPTION KEY: ");
        System.out.println(rsaEncrypt(encryptionKey));

        String plainText = "let's get shit shiity fuck done";


        try {
            byte[] cipher = encrypt(plainText, encryptionKey);

            System.out.print("ENCODED STRING: ");
            System.out.println(Base64.encodeToString(cipher, Base64.DEFAULT));


        } catch (Exception ev) {
            ev.printStackTrace();
        }

    }


    /***** AES ******/
    public static byte[] encrypt(String plainText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        return cipher.doFinal(plainText.getBytes("UTF-8"));
    }

    public static String decrypt(byte[] cipherText, String encryptionKey) throws Exception{
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
        cipher.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8")));
        return new String(cipher.doFinal(cipherText),"UTF-8");
    }

    private String generateKeyString() {
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

          //  byte[] encryptedBytes = cipher.doFinal(str.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return encrypted;
    }


    private SecretKeySpec secretKeyFromBytes(byte[] secretBytes) {
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretBytes, "AES");

        return secretKeySpec;
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

    private PrivateKey privateKeyFromBytes(byte[] privateBytes) {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void printKeys(PublicKey publicKey, PrivateKey privateKey) {
        byte[] pubEnc = publicKey.getEncoded();
        String s = Base64.encodeToString(pubEnc, Base64.DEFAULT);

        byte[] privEnc = privateKey.getEncoded();
        String b = Base64.encodeToString(privEnc, Base64.DEFAULT);

        System.out.print(s);
        System.out.println("----------");
        System.out.print(b);
    }

    private String[] generateKeyStrings() throws NoSuchAlgorithmException {
        String[] keyPair = new String[2];

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair kp = kpg.genKeyPair();

        PublicKey publicKey = kp.getPublic();
        PrivateKey privateKey = kp.getPrivate();

        byte[] publicBytes = publicKey.getEncoded();
        String publicString = Base64.encodeToString(publicBytes, Base64.DEFAULT);
        keyPair[0] = publicString;

        byte[] privateBytes = privateKey.getEncoded();
        String privateString = Base64.encodeToString(privateBytes, Base64.DEFAULT);
        keyPair[1] = privateString;

        return keyPair;
    }

}
