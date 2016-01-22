package com.example.conferencemanager.utilities;

/**
 * Created by Narcis11 on 21/01/2016.
 * This is where we store the constants that are used globally.
 */
public class Constants {

    //preferences key
    public static final String PREF_CREDENTIALS = "PREFERENCE_CREDENTIALS";
    public static final String PREF_CREDENTIALS_KEY = "123qwe456tyuzxc";

    //the user types
    public static final String ADMIN_USER_TYPE = "Admin";
    public static final String DOCTOR_USER_TYPE = "Doctor";
    /*********************************USED FOR THE ADMIN SIDE***********************************/
    //used to determine whether the admin is already logged in
    public static final String PREF_IS_ADMIN_LOGGED_IN_KEY = "pref_is_admin_logged_in_key";
    public static final String PREF_IS_ADMIN_LOGGED_IN_TRUE = "pref_is_admin_logged_in_true";

    //used to save the admin's username in the shared preferences
    public static final String PREF_ADMIN_USERNAME_KEY = "pref_admin_username_key";

    //used in bundle communications
    public static final String BUNDLE_ADMIN_CONF_TITLE_KEY = "bundle_admin_conf_title_key";
    public static final String BUNDLE_ADMIN_CONF_ID_KEY = "bundle_admin_conf_id_key";
    public static final String BUNDLE_ADMIN_CONF_ADDRESS_KEY = "bundle_admin_conf_address_key";
    public static final String BUNDLE_ADMIN_CONF_DATE_KEY = "bundle_admin_conf_date_key";
    public static final String BUNDLE_ADMIN_CONF_DESCRIPTION_KEY = "bundle_admin_conf_description_key";

    /*********************************USED FOR THE DOCTOR SIDE***********************************/
    //used to determine whether the doctor is already logged in
    public static final String PREF_IS_DOCTOR_LOGGED_IN_KEY = "pref_is_doctor_logged_in_key";
    public static final String PREF_IS_DOCTOR_LOGGED_IN_TRUE = "pref_is_doctor_logged_in_true";

    //used to save the doctor's username in the shared preferences
    public static final String PREF_DOCTOR_USERNAME_KEY = "pref_doctor_username_key";
}
