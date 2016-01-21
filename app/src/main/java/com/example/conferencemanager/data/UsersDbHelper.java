package com.example.conferencemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Narcis11 on 20/01/2016.
 */
public class UsersDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "conf.db";

    public UsersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_USERS_TABLE = "CREATE TABLE " + UsersContract.UsersEntry.TABLE_NAME + " (" +
                UsersContract.UsersEntry._ID + " INTEGER PRIMARY KEY, " +
                UsersContract.UsersEntry.COLUMN_USERNAME + " TEXT, " +
                UsersContract.UsersEntry.COLUMN_PASSWORD + " TEXT, " +
                UsersContract.UsersEntry.COLUMN_USER_TYPE + " TEXT " + ");";

        db.execSQL(SQL_CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
