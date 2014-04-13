package com.comp3111.localendar.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static android.provider.BaseColumns._ID;
import static com.comp3111.localendar.database.DatabaseConstants.COMPULSORY;
import static com.comp3111.localendar.database.DatabaseConstants.DAY;
import static com.comp3111.localendar.database.DatabaseConstants.DESCRIPTION;
import static com.comp3111.localendar.database.DatabaseConstants.DURATION_HOUR;
import static com.comp3111.localendar.database.DatabaseConstants.DURATION_MINUTE;
import static com.comp3111.localendar.database.DatabaseConstants.HOUR;
import static com.comp3111.localendar.database.DatabaseConstants.LOCATION;
import static com.comp3111.localendar.database.DatabaseConstants.MINUTE;
import static com.comp3111.localendar.database.DatabaseConstants.MONTH;
import static com.comp3111.localendar.database.DatabaseConstants.TABLE_NAME;
import static com.comp3111.localendar.database.DatabaseConstants.TITLE;
import static com.comp3111.localendar.database.DatabaseConstants.TRANSPORTATION;
import static com.comp3111.localendar.database.DatabaseConstants.YEAR;

public class DatabaseHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "event.db";
    private final static int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    	
        final String INIT_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                                  _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                  TITLE + " TEXT, " +
                                  DESCRIPTION + " TEXT, " +
                                  YEAR + " TEXT, " +
                                  MONTH + " TEXT, " +
                                  DAY + " TEXT, " +
                                  HOUR + " TEXT, " +
                                  MINUTE + " TEXT, " +
                                  DURATION_HOUR + " TEXT, " +
                                  DURATION_MINUTE + " TEXT, " +
                                  LOCATION + " TEXT, " +
                                  TRANSPORTATION + " TEXT, " +
                                  COMPULSORY + " TEXT);"; 
                                  
    	
        db.execSQL(INIT_TABLE);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
	
	
}
