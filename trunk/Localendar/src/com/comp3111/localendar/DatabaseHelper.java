package com.comp3111.localendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;
import static com.comp3111.localendar.DatabaseConstants.TABLE_NAME;
import static com.comp3111.localendar.DatabaseConstants.TITLE;
import static com.comp3111.localendar.DatabaseConstants.DESCRIPTION;
import static com.comp3111.localendar.DatabaseConstants.YEAR;
import static com.comp3111.localendar.DatabaseConstants.MONTH;
import static com.comp3111.localendar.DatabaseConstants.DAY;
import static com.comp3111.localendar.DatabaseConstants.TRANSPORTATION;
import static com.comp3111.localendar.DatabaseConstants.LOCATION;
import static com.comp3111.localendar.DatabaseConstants.COMPULSORY;

public class DatabaseHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "event.db";
    private final static int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	/*
        final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                                  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                  TITLE + " TEXT, " +
                                  DESCRIPTION + " TEXT, " +
                                  YEAR + " INTEGER, " +
                                  MONTH + " INTEGER, " +
                                  DAY + " DAY, " +
                                  TRANSPORTATION + " TEXT, " +
                                  LOCATION + " TEXT, " +
                                  COMPULSORY + " TEXT);"; 
                                  */
    	final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TITLE + " TEXT, " +
                DESCRIPTION + " TEXT); ";
        db.execSQL(INIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

	
}
