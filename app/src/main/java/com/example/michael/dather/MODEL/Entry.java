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

    public Entry(String ts, String light, String steps, String volume, String accX, String accY, String accZ, String lati, String longi) {
        this.ts     = ts;
        this.light  = light;
        this.steps  = steps;
        this.volume = volume;
        this.accX   = accX;
        this.accY   = accY;
        this.accZ   = accZ;
        this.lati   = lati;
        this.longi  = longi;
    }
}
