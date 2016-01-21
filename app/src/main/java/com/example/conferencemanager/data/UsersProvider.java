package com.example.conferencemanager.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Narcis11 on 21/01/2016.
 */
public class UsersProvider extends ContentProvider {

    private UsersDbHelper mUsersHelper;
    final String LOG_TAG = UsersProvider.class.getSimpleName();
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int USERS = 100;

    @Override
    public boolean onCreate() {
        mUsersHelper = new UsersDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case USERS: {
                cursor = mUsersHelper.getReadableDatabase().query(
                        UsersContract.UsersEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //Tell the cursor to register a Content Observer to watch for changes that appear in that uri or any of its descendants
        //descendants = uris that are like 'uri%'
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS: return UsersContract.UsersEntry.CONTENT_TYPE;
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase sqLiteDatabase = mUsersHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case USERS: {
                long _id = sqLiteDatabase.insert(UsersContract.UsersEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = UsersContract.UsersEntry.buildUsersUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase = mUsersHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS: {
                rowsDeleted = sqLiteDatabase.delete(UsersContract.UsersEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        //a null selection deletes all rows
        //we only notify if rows were indeed deleted
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase sqLiteDatabase = mUsersHelper.getWritableDatabase();
        int rowsUpdated;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS: {
                rowsUpdated = sqLiteDatabase.update(UsersContract.UsersEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase sqLiteDatabase = mUsersHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case USERS: {
                sqLiteDatabase.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = sqLiteDatabase.insert(UsersContract.UsersEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    sqLiteDatabase.setTransactionSuccessful();
                } catch (IllegalStateException e) {
                    //   Log.e(LOG_TAG, "Error while inserting bulk! = " + e.getMessage());
                    e.printStackTrace();
                    return super.bulkInsert(uri, values);//in case of errors, we call the regular insert method
                } finally {
                    sqLiteDatabase.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = UsersContract.CONTENT_AUTHORITY;

        // For each type of URI we want to add, we create a corresponding code.
        matcher.addURI(authority, UsersContract.PATH_USERS, USERS);
        return matcher;
    }
}
