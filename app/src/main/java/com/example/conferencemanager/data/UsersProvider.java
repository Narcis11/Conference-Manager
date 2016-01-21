package com.example.conferencemanager.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by Narcis11 on 21/01/2016.
 */
public class UsersProvider extends ContentProvider {

    private UsersDbHelper mUsersHelper;
    final String LOG_TAG = UsersProvider.class.getSimpleName();
    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int USERS = 100;
    private static final int CONFERENCES = 101;
    private static final int TOPICS = 102;
    private static final int INVITES = 103;

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
            case CONFERENCES: {
                cursor = mUsersHelper.getReadableDatabase().query(
                        UsersContract.ConferencesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        UsersContract.ConferencesEntry.SORT_ORDER);
                break;
            }
            case TOPICS: {
                cursor = mUsersHelper.getReadableDatabase().query(
                        UsersContract.TopicsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        UsersContract.TopicsEntry.SORT_ORDER);
                break;
            }
            case INVITES: {
                cursor = mUsersHelper.getReadableDatabase().query(
                        UsersContract.InvitesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        UsersContract.InvitesEntry.SORT_ORDER);
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
            case CONFERENCES: return UsersContract.ConferencesEntry.CONTENT_TYPE;
            case TOPICS: return UsersContract.TopicsEntry.CONTENT_TYPE;
            case INVITES: return UsersContract.InvitesEntry.CONTENT_TYPE;
            default: throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase sqLiteDatabase = mUsersHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        Log.i(LOG_TAG,"In insert, uri/match : " + uri.toString() + "/" + match);
        switch (match) {
            case USERS: {
                long _id = sqLiteDatabase.insert(UsersContract.UsersEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = UsersContract.UsersEntry.buildUsersUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case CONFERENCES: {
                long _id = sqLiteDatabase.insert(UsersContract.ConferencesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = UsersContract.ConferencesEntry.buildConferencesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case TOPICS: {
                long _id = sqLiteDatabase.insert(UsersContract.TopicsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = UsersContract.TopicsEntry.buildTopicsUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case INVITES: {
                long _id = sqLiteDatabase.insert(UsersContract.InvitesEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = UsersContract.InvitesEntry.buildInvitesUri(_id);
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
            case CONFERENCES: {
                rowsDeleted = sqLiteDatabase.delete(UsersContract.ConferencesEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TOPICS: {
                rowsDeleted = sqLiteDatabase.delete(UsersContract.TopicsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case INVITES: {
                rowsDeleted = sqLiteDatabase.delete(UsersContract.InvitesEntry.TABLE_NAME, selection, selectionArgs);
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
            case CONFERENCES: {
                rowsUpdated = sqLiteDatabase.update(UsersContract.ConferencesEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case TOPICS: {
                rowsUpdated = sqLiteDatabase.update(UsersContract.TopicsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            case INVITES: {
                rowsUpdated = sqLiteDatabase.update(UsersContract.InvitesEntry.TABLE_NAME, values, selection, selectionArgs);
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
            case CONFERENCES: {
                sqLiteDatabase.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = sqLiteDatabase.insert(UsersContract.ConferencesEntry.TABLE_NAME, null, value);
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
            case TOPICS: {
                sqLiteDatabase.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = sqLiteDatabase.insert(UsersContract.TopicsEntry.TABLE_NAME, null, value);
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
            case INVITES: {
                sqLiteDatabase.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = sqLiteDatabase.insert(UsersContract.InvitesEntry.TABLE_NAME, null, value);
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
        matcher.addURI(authority, UsersContract.PATH_CONFERENCES, CONFERENCES);
        matcher.addURI(authority, UsersContract.PATH_TOPICS, TOPICS);
        matcher.addURI(authority, UsersContract.PATH_INVITES, INVITES);
        return matcher;
    }
}
