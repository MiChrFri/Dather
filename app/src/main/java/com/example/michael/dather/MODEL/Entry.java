package com.example.michael.dather.MODEL;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

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

    public Entry(String[] entryVals) {
        if(entryVals.length == 9) {
            this.ts     = entryVals[0];
            this.light  = entryVals[1];
            this.steps  = entryVals[2];
            this.volume = entryVals[3];
            this.accX   = entryVals[4];
            this.accY   = entryVals[5];
            this.accZ   = entryVals[6];
            this.lati   = entryVals[7];
            this.longi  = entryVals[8];
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
        }
    }
}
