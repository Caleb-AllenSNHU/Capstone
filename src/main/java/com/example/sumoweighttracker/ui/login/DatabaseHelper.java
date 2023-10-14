//File Name: DatabaseHelper.java
//Version: 1.0
//Date: 10/08/2023
//Author: Caleb Allen
//Comments: Created to add more functionality to the template database. This gives a place to define
//Database variables and contain the methods that are called from the main database.java file

package com.example.sumoweighttracker.ui.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String COLUMN_TIMESTAMP = String.valueOf(1);
    // update to place timestamps on all entries to call back later and increase query complexity
    private static final String DATABASE_NAME = "entries.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_ENTRIES = "entries";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ENTRY = "entry";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_ENTRIES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_ENTRY
            + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ENTRIES);
        onCreate(db);
    }
}
