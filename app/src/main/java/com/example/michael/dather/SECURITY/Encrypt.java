package com.example.michael.dather.SECURITY;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.example.michael.dather.MODEL.Entry;

import java.io.ByteArrayOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by michael on 10/06/16.
 */

public class Encrypt {

    private SecretKeySpec sks = null;

    public Encrypt() {
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
            Log.i("DECRYOTED", String.valueOf(symDecrypt(encrypted)));
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
        // Decode the encoded data with AES
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
}
