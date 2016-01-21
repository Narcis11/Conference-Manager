package com.example.conferencemanager.test;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;
import android.util.Log;

import com.example.conferencemanager.data.UsersContract;
import com.example.conferencemanager.data.UsersDbHelper;

/**
 * Created by Narcis11 on 20/01/2016.
 */
public class UsersTest extends AndroidTestCase {

    private static final String LOG_TAG = UsersTest.class.getSimpleName();

    public void testCreateDb() throws Throwable {
        mContext.deleteDatabase(UsersDbHelper.DATABASE_NAME);
        SQLiteDatabase sqLiteDatabase = new UsersDbHelper(mContext).getReadableDatabase();
        assertEquals(sqLiteDatabase.isOpen(), true);
        sqLiteDatabase.close();
    }

    public void testInsertUsers() throws Throwable {
        ContentValues insertValues = new ContentValues();
        insertValues.put(UsersContract.UsersEntry.COLUMN_USERNAME, "test user");
        insertValues.put(UsersContract.UsersEntry.COLUMN_USER_TYPE, "Admin");
        Uri insertUri = mContext.getContentResolver().insert(UsersContract.UsersEntry.CONTENT_URI, insertValues);
        long positionId = ContentUris.parseId(insertUri);
        Log.i(LOG_TAG, "Insert uri is: " + insertUri);
        Log.i(LOG_TAG, "Row number inserted = " + positionId);
        assertTrue(positionId != -1);
    }

    public void testInsertConferences() throws Throwable {
        ContentValues insertValues = new ContentValues();
        insertValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_TITLE, "Test conference");
        insertValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_ADDRESS, "Wayoming Street, no. 67");
        insertValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_ADDED_BY, "test");
        insertValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_DESCRIPTION, "The conference will take place in the big event hall. Bring your" +
                "own snack. No entrance fee.");
        insertValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_DATE, "test");
        Uri insertUri = mContext.getContentResolver().insert(UsersContract.UsersEntry.CONTENT_URI, insertValues);
        long positionId = ContentUris.parseId(insertUri);
        Log.i(LOG_TAG, "Insert uri is: " + insertUri);
        Log.i(LOG_TAG, "Row number inserted = " + positionId);
        assertTrue(positionId != -1);
    }

    public void testDeleteUsers() throws Throwable {
        String deleteCriteria = "test user";
        String[] querySelectionArgs = new String[1];
        querySelectionArgs[0] = deleteCriteria;
        int deletedNo = mContext.getContentResolver().delete(UsersContract.UsersEntry.CONTENT_URI, UsersContract.UsersEntry.COLUMN_USERNAME + " = ?",
                querySelectionArgs);
        Log.i(LOG_TAG, "Delete no: " + deletedNo);
    }
}
