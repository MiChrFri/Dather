package com.example.michael.dather.MODEL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by michael on 03/06/16.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME    = "dataset.db";
    private static final int DATABASE_VERSION   = 1;

    public static final String TABLE_NAME       = "entrys";

    public static final String COLUMN_ID        = "id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_LIGHT     = "light";
    public static final String COLUMN_STEPS     = "steps";

    public static final String COLUMN_VOLUME    = "volume";
    public static final String COLUMN_ACCX      = "accX";
    public static final String COLUMN_ACCY      = "accY";
    public static final String COLUMN_ACCZ      = "accZ";
    public static final String COLUMN_LATITUDE  = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY, " +
                COLUMN_TIMESTAMP + " TEXT, " +
                COLUMN_LIGHT + " TEXT, " +
                COLUMN_STEPS + " TEXT, " +
                COLUMN_VOLUME + " TEXT, " +
                COLUMN_ACCX + " TEXT, " +
                COLUMN_ACCY + " TEXT, " +
                COLUMN_ACCZ + " TEXT, " +
                COLUMN_LATITUDE + " TEXT, " +
                COLUMN_LONGITUDE + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertEntry(Entry entry) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TIMESTAMP, entry.ts);
        contentValues.put(COLUMN_LIGHT,     entry.light);
        contentValues.put(COLUMN_STEPS,     entry.steps);
        contentValues.put(COLUMN_VOLUME,    entry.volume);
        contentValues.put(COLUMN_ACCX,      entry.accX);
        contentValues.put(COLUMN_ACCY,      entry.accY);
        contentValues.put(COLUMN_ACCZ,      entry.accZ);
        contentValues.put(COLUMN_LATITUDE,  entry.lati);
        contentValues.put(COLUMN_LONGITUDE, entry.longi);

        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateEntry(int id, Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_TIMESTAMP, entry.ts);
        contentValues.put(COLUMN_LIGHT,     entry.light);
        contentValues.put(COLUMN_STEPS,     entry.steps);
        contentValues.put(COLUMN_VOLUME,    entry.volume);
        contentValues.put(COLUMN_ACCX,      entry.accX);
        contentValues.put(COLUMN_ACCY,      entry.accY);
        contentValues.put(COLUMN_ACCZ,      entry.accZ);
        contentValues.put(COLUMN_LATITUDE,  entry.lati);
        contentValues.put(COLUMN_LONGITUDE, entry.longi);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + " = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Cursor getEntry(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COLUMN_ID + "=?", new String[] { Integer.toString(id) } );
        return res;
    }

    public String getUserID() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_NAME + " ASC LIMIT 1", null );

        ArrayList<ArrayList<String>> userID = cursorToArrays(res);

        Log.i("USER ID", userID.toString());

        return toString();
    }

    public ArrayList<ArrayList<String>> getAllEntries() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );

        return cursorToArrays(res);
    }

    private ArrayList<ArrayList<String>> cursorToArrays(Cursor crsr) {
        ArrayList<ArrayList<String>> results = new ArrayList<ArrayList<String>>();

        if(crsr.getCount() != 0) {
            while(crsr.moveToNext()) {
                ArrayList<String> entry = new ArrayList<String>();

                for(int i = 0; i < crsr.getColumnCount(); i++) {
                    entry.add(crsr.getString(i));
                }
                results.add(entry);
            }
        }
        return results;
    }

    public Boolean hasEntries() {
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery( "SELECT * FROM " + TABLE_NAME, null ).getCount() != 0;
    }

    public void clearTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM "+ TABLE_NAME);
    }
}
