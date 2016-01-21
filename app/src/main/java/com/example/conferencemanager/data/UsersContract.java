package com.example.conferencemanager.data;

import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Narcis11 on 20/01/2016.
 */
public class UsersContract {
    public static final String CONTENT_AUTHORITY = "com.example.conferencemanager";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //used for determining which path to choose
    public static final String PATH_USERS = "users";
    public static final String PATH_CONFERENCES = "conferences";

    public static final class UsersEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_USERNAME = "username";//text
        public static final String COLUMN_PASSWORD = "password";//text
        public static final String COLUMN_EMAIL = "email";//text
        public static final String COLUMN_USER_TYPE = "user_type";//text

        //used for the content provider
        //content://com.example.conferencemanager/users
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_USERS).build();
        //vnd.android.cursor.dir/com.example.conferencemanager/users
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_USERS;
        //vnd.android.cursor.item/com.example.conferencemanager/users
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_USERS;
        //used for manipulating a single row
        public static Uri buildUsersUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ConferencesEntry implements BaseColumns {
        public static final String TABLE_NAME = "conferences";
        public static final String COLUMN_TITLE = "title";//text
        public static final String COLUMN_DESCRIPTION = "description";//text
        public static final String COLUMN_ADDRESS = "address";//text
        public static final String COLUMN_DATE = "date";//text
        public static final String COLUMN_ADDED_BY = "added_by";//text - the username of the person who added the conference

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONFERENCES).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CONFERENCES;
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CONFERENCES;
        public static Uri buildConferencesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
