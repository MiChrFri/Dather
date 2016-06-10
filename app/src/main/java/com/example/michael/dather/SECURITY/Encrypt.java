package com.example.michael.dather.SECURITY;

import android.util.Base64;
import android.util.Log;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by michael on 10/06/16.
 */

public class Encrypt {
    private SecretKeySpec sks = null;

    public Encrypt() {

        doBalalala();

        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
            Log.e("ERROR", "AES secret key spec error");
        }
    }

    public void entry(String[] entrieVals ) {
        for(String entrieVal : entrieVals) {
            Log.i("NORMAL", entrieVal);
            byte[] encrypted = symmetric(entrieVal);
            Log.i("DECRYPTED", String.valueOf(symDecrypt(encrypted)));
        }
    }

    private byte[] symmetric(String message) {
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encodedBytes = c.doFinal(message.getBytes());
        } catch (Exception e) {
            Log.e("ERROR", "AES encryption error");
        }

        String encrypted = Base64.encodeToString(encodedBytes, Base64.DEFAULT);
        Log.i("ENCRYPTED", encrypted);

        return encodedBytes;
    }

    private String symDecrypt(byte[] encodedBytes) {
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) {
            Log.e("ERROR", "AES decryption error");
        }

        return new String(decodedBytes);
    }











    public void doBalalala() {
        String str = "hihihi";

        String pk = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCOL3WULzV3CQz8AqttArEu3IsW7acVeZkgMJtWgOwMpaeTDL3vthqV++UH2Eu+sHv7fHP5uI1CC//V6QfF45CFWtBV3LA2/hG2rRgI1bHmiP5adG+2hWSPB+0tVx3yhR8K0QqV2F8VQF82w2PuVcfj0KWWgQYloxd6rxSh/PR0bQIDAQAB";


        PublicKey pkey = null;
        try {
            pkey = getFromString(pk);

            byte [] encryptedBytes;
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pkey);
            encryptedBytes = cipher.doFinal(str.getBytes());
            String encrypted = new String(encryptedBytes);

            Log.i("----", encrypted);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public PublicKey getFromString(String keystr) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte [] encoded = Base64.decode(keystr,  Base64.DEFAULT);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pubkey = kf.generatePublic(keySpec);

        return pubkey;
    }

//    public String RSAEncrypt(final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,
//            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
//
//        if (pubKey!=null) {
//            cipher = Cipher.getInstance("RSA");
//            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
//            encryptedBytes = cipher.doFinal(plain.getBytes());
//            Log.d("BYTES", new String(encryptedBytes));
//            return Hex.encodeHexString(encryptedBytes);
//        }
//        else
//            return null;
//    }

}
