package com.example.michael.dather.MODEL;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.michael.dather.SECURITY.Encrypt;

/**
 * Created by michael on 03/06/16.
 */

public class Entry {
    String ts;
    String light;
    String steps;
    String volume;
    String accX;
    String accY;
    String accZ;
    String lati;
    String longi;
    String secret;

    public Entry(String[] entryVals, Encrypt encrypt) {
        if(entryVals.length == 9) {

            this.ts     = entryVals[0];
            this.light  = encrypt.encryptString(entryVals[1]);
            this.steps  = encrypt.encryptString(entryVals[2]);
            this.volume = encrypt.encryptString(entryVals[3]);
            this.accX   = encrypt.encryptString(entryVals[4]);
            this.accY   = encrypt.encryptString(entryVals[5]);
            this.accZ   = encrypt.encryptString(entryVals[6]);
            this.lati   = encrypt.encryptString(entryVals[7]);
            this.longi  = encrypt.encryptString(entryVals[8]);
            this.secret = encrypt.getEncryptedSecret();
        }
        else {
            this.ts     = "ENTRY ERROR";
            this.light  = "ENTRY ERROR";
            this.steps  = "ENTRY ERROR";
            this.volume = "ENTRY ERROR";
            this.accX   = "ENTRY ERROR";
            this.accY   = "ENTRY ERROR";
            this.accZ   = "ENTRY ERROR";
            this.lati   = "ENTRY ERROR";
            this.longi  = "ENTRY ERROR";
            this.secret = "ENTRY ERROR";
        }
    }
}
