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
                UsersContract.UsersEntry.COLUMN_EMAIL + " TEXT, " +
                UsersContract.UsersEntry.COLUMN_USER_TYPE + " TEXT " + ");";

        final String SQL_CREATE_CONFERENCES_TABLE = "CREATE TABLE " + UsersContract.ConferencesEntry.TABLE_NAME + " (" +
                UsersContract.ConferencesEntry._ID + " INTEGER PRIMARY KEY, " +
                UsersContract.ConferencesEntry.COLUMN_CONF_TITLE + " TEXT, " +
                UsersContract.ConferencesEntry.COLUMN_CONF_DESCRIPTION + " TEXT, " +
                UsersContract.ConferencesEntry.COLUMN_CONF_ADDED_BY + " TEXT, " +
                UsersContract.ConferencesEntry.COLUMN_CONF_DATE + " TEXT, " +
                UsersContract.ConferencesEntry.COLUMN_CONF_ADDRESS + " TEXT " + ");";

        final String SQL_CREATE_TOPICS_TABLE = "CREATE TABLE " + UsersContract.TopicsEntry.TABLE_NAME + " (" +
                UsersContract.TopicsEntry._ID + " INTEGER PRIMARY KEY, " +
                UsersContract.TopicsEntry.COLUMN_TOPIC_TITLE + " TEXT, " +
                UsersContract.TopicsEntry.COLUMN_TOPIC_DESCRIPTION + " TEXT, " +
                UsersContract.TopicsEntry.COLUMN_TOPIC_ADDED_BY + " TEXT " + ");";

        final String SQL_CREATE_INVITES_TABLE = "CREATE TABLE " + UsersContract.InvitesEntry.TABLE_NAME + " (" +
                UsersContract.InvitesEntry._ID + " INTEGER PRIMARY KEY, " +
                UsersContract.InvitesEntry.COLUMN_CONF_NAME + " TEXT, " +
                UsersContract.InvitesEntry.COLUMN_RECIPIENT + " TEXT, " +
                UsersContract.InvitesEntry.COLUMN_SENDER + " TEXT " + ");";

        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_CONFERENCES_TABLE);
        db.execSQL(SQL_CREATE_TOPICS_TABLE);
        db.execSQL(SQL_CREATE_INVITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
