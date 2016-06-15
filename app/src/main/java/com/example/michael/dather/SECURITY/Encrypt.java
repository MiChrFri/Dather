package com.example.michael.dather.SECURITY;

import android.util.Base64;
import android.util.Log;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by michael on 10/06/16.
 */

public class Encrypt {
    private SecretKeySpec sks = null;


    /******* Syncronous encryption *******/
    public Encrypt() {

        rsaEncrypt();
//
//        try {
//            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
//            sr.setSeed("any data used as random seed".getBytes());
//            KeyGenerator kg = KeyGenerator.getInstance("AES");
//            kg.init(128, sr);
//            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
//        } catch (Exception e) {
//            Log.e("ERROR", "AES secret key spec error");
//        }
//    }
//
//    public void entry(String[] entrieVals ) {
//        for(String entrieVal : entrieVals) {
//            //Log.i("NORMAL", entrieVal);
//            byte[] encrypted = aesEncrypt(entrieVal);
//            //Log.i("DECRYPTED", String.valueOf(aesDecrypt(encrypted)));
//        }
    }

    private byte[] aesEncrypt(String message) {
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encodedBytes = c.doFinal(message.getBytes());
        } catch (Exception e) {
            Log.e("ERROR", "AES encryption error");
        }

        String encrypted = Base64.encodeToString(encodedBytes, Base64.DEFAULT);
        //Log.i("ENCRYPTED", encrypted);

        return encodedBytes;
    }


    private void rsaEncrypt() {
        String str = "hihihi";

        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        try {
            //String[] keys = generateKeyStrings()

            String publicString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFok0ykRoHsXZE/DZKvvDqr+tabRGNSbk7xheE\n" +
                    "EuKaA3FXsXgo5HXyNEuSammdSkwI34cEHCn9W1AKnZ5XvKRlyE3TzehUxQ3dk5Fp0CqI+bh9w9XF\n" +
                    "7I++yzuBn+4pcEyu/I5pCvNaLxkoOLOLBSP7+jY60VD2BOtkSbc3knCLDwIDAQAB";

            String privateString = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMWiTTKRGgexdkT8Nkq+8Oqv61pt\n" +
                    "EY1JuTvGF4QS4poDcVexeCjkdfI0S5JqaZ1KTAjfhwQcKf1bUAqdnle8pGXITdPN6FTFDd2TkWnQ\n" +
                    "Koj5uH3D1cXsj77LO4Gf7ilwTK78jmkK81ovGSg4s4sFI/v6NjrRUPYE62RJtzeScIsPAgMBAAEC\n" +
                    "gYB1NfC60LuzXFhcSYiZg+y2A2d91bnXJaYqAS3pMpoZ5XjPHX26IuSgpMs9JsdEdc/qY0db9Kgi\n" +
                    "jolGAnzqZALLHbk4LcM1Faf6ykZQp52yCWWZQYPHk43S/p2k+krChmZjb2LD2uzzO32nWPfk+r9m\n" +
                    "Fz7KRhmqdr7KqvWBcuD3IQJBAP8YUYfOVJOkItgPxpdsZXs5g0GD89dkXkyY/ZRwLK5G4Yt6/02N\n" +
                    "ac7lJasLkPnEKuY6ECs6hMOxLsbnF1YvsvMCQQDGVcvI4pPGeCM0RGrh0CyrGfp9EezoKXaYgIGf\n" +
                    "G6/EuvFQDOJkNo4u02HNO6hAFtw7LnNHFf2lznj/MHgRqbZ1AkEAlL9Sc1VOHhVcuA5i59MuTa9Y\n" +
                    "qTBPVK7TCelAHHlYpHryc8nR6x/lrd4Sm+2PqQTJWxxKZ1qlJhNASn1gL0J7HwJARwrmYv+d9UKF\n" +
                    "QaycKGS8C1HT4sbv+D0Z6Qhm5coyDKL12zmyQjk0dttqtev/mW1W17AMxNLAt4qc1rPS3mjPHQJB\n" +
                    "AL95KMDVYjtru+UqLXvbx8atVOQQRGZ+dWasGix4y9+ux8uWGJ69c3wWWp4K5+KTtsce1uI1UAtg\n" +
                    "iXs11/t7IZY=";

            /**** create the keys from the Strings ****/
            byte[] publicBytes = Base64.decode(publicString, Base64.DEFAULT);
            publicKey = publicKeyFromBytes(publicBytes);

            byte[] privateBytes = Base64.decode(privateString, Base64.DEFAULT);
            privateKey = privateKeyFromBytes(privateBytes);

            /***** Encrypt *****/
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] cipherData = cipher.doFinal(str.getBytes());
            String encryptedStringBase64 = Base64.encodeToString(cipherData, Base64.DEFAULT);
            System.out.println("Encrypted: " + encryptedStringBase64 + "|");


            byte[] encryptedBytes = cipher.doFinal(str.getBytes());

            /***** Decrypt *****/
            Cipher cipher1 = Cipher.getInstance("RSA");

            cipher1.init(Cipher.DECRYPT_MODE, privateKey);
            byte [] decryptedBytes = cipher1.doFinal(encryptedBytes);
            String decrypted = new String(decryptedBytes);
            System.out.println("Decrypted "+ decrypted);


        } catch (Exception e) {
            e.printStackTrace();
        }
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
