package com.example.conferencemanager.admin;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.conferencemanager.R;
import com.example.conferencemanager.utilities.Constants;
import com.example.conferencemanager.utilities.SecurePreferences;
import com.example.conferencemanager.utilities.Utility;
import com.example.conferencemanager.data.UsersContract;

import java.util.Vector;

public class AdminSignUpActivity extends AppCompatActivity {

    private final String LOG_TAG = AdminSignUpActivity.class.getSimpleName();
    private Context mContext;
    //the layout that contains the input fields
    private RelativeLayout mInputFieldsLayout;
    //the edittext views
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private EditText mConfirmPasswordEditText;
    private EditText mEmailEditText;
    //the edit text values
    private String mPasswordValue;
    //the error messages
    private TextView mUsernameError;
    private TextView mPasswordError;
    private TextView mConfirmPasswordError;
    private TextView mEmailError;
    //the next button
    private Button mSignUpButton;
    //generic error message
    private static String EMPTY_FIELD_ERROR = "";
    private SecurePreferences mSecurePreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = getApplicationContext();
        EMPTY_FIELD_ERROR = getResources().getString(R.string.no_input);
        mSecurePreferences = new SecurePreferences(mContext, Constants.PREF_CREDENTIALS, Constants.PREF_CREDENTIALS_KEY, true);
        loadUIElements();
        //set the onClick listener for the sign button
        //since there's only one click listener, there's no point in defining a separate method
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllFields();
            }
        });
    }

    /**************************************START OF UI METHODS*****************************************/
    private void loadUIElements() {
        mInputFieldsLayout = (RelativeLayout) findViewById(R.id.admin_signup_fields_layout);
        //the edittext views
        mUsernameEditText = (EditText) mInputFieldsLayout.findViewById(R.id.admin_signup_username_value);
        mPasswordEditText = (EditText) mInputFieldsLayout.findViewById(R.id.admin_signup_password_value);
        mConfirmPasswordEditText = (EditText) mInputFieldsLayout.findViewById(R.id.admin_signup_confirm_password_value);
        mEmailEditText = (EditText) mInputFieldsLayout.findViewById(R.id.admin_signup_email_value);
        //the error textviews
        mUsernameError = (TextView) mInputFieldsLayout.findViewById(R.id.admin_signup_username_error);
        mPasswordError = (TextView) mInputFieldsLayout.findViewById(R.id.admin_signup_password_error);
        mConfirmPasswordError = (TextView) mInputFieldsLayout.findViewById(R.id.admin_signup_confirm_password_error);
        mEmailError = (TextView) mInputFieldsLayout.findViewById(R.id.admin_signup_email_error);
        //the sign up button
        mSignUpButton = (Button) findViewById(R.id.admin_signup_button);
    }

    /**************************************END OF UI METHODS*****************************************/

    /*****************************************START OF CHECK METHODS*************************************/

    private boolean checkUsernameField() {
        if (mUsernameEditText.getText().toString().length() == 0) {
            mUsernameError.setVisibility(View.VISIBLE);
            mUsernameError.setText(EMPTY_FIELD_ERROR);
            mUsernameEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            return false;
        }
        else {
            if (mUsernameError.getVisibility() == View.VISIBLE) {
                mUsernameError.setVisibility(View.INVISIBLE);
                mUsernameError.setText("");
                mUsernameEditText.getBackground().setColorFilter(getResources().getColor(R.color.material_grey_800), PorterDuff.Mode.SRC_IN);
                return true;
            }
        }
        return true;
    }

    private boolean checkPasswordField() {
        if (mPasswordEditText.getText().toString().length() == 0) {
            mPasswordError.setVisibility(View.VISIBLE);
            mPasswordError.setText(EMPTY_FIELD_ERROR);
            mPasswordEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            return false;
        }
        else if (mPasswordEditText.getText().toString().length() < 7) {
            mPasswordError.setVisibility(View.VISIBLE);
            mPasswordError.setText(getResources().getString(R.string.signup_short_password));
            mPasswordEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            return false;
        }
        else {
            if (mPasswordError.getVisibility() == View.VISIBLE) {
                mPasswordError.setVisibility(View.INVISIBLE);
                mPasswordError.setText("");
                mPasswordEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.material_grey_800), PorterDuff.Mode.SRC_IN);
                return true;
            }
        }
        return true;
    }

    private boolean checkConfirmPasswordField() {
        if (mConfirmPasswordEditText.getText().toString().length() == 0) {
            mConfirmPasswordError.setVisibility(View.VISIBLE);
            mConfirmPasswordError.setText(EMPTY_FIELD_ERROR);
            mConfirmPasswordEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            return false;
        }
        else if (!mPasswordEditText.getText().toString().equals(mConfirmPasswordEditText.getText().toString())) {
            mConfirmPasswordError.setVisibility(View.VISIBLE);
            mConfirmPasswordError.setText(getResources().getString(R.string.signup_password_mismatch));
            mConfirmPasswordEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            return false;
        }
        else {
            if (mConfirmPasswordError.getVisibility() == View.VISIBLE) {
                mConfirmPasswordError.setVisibility(View.INVISIBLE);
                mConfirmPasswordError.setText("");
                mConfirmPasswordEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.material_grey_800), PorterDuff.Mode.SRC_IN);
                return true;
            }
        }
        return true;
    }

    private boolean checkEmailField() {
        if (mEmailEditText.getText().toString().length() == 0) {
            mEmailError.setVisibility(View.VISIBLE);
            mEmailError.setText(EMPTY_FIELD_ERROR);
            mEmailEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(mEmailEditText.getText().toString()).matches() ) {
            mEmailError.setVisibility(View.VISIBLE);
            mEmailError.setText(getResources().getString(R.string.registration_invalid_email));
            mEmailEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            return false;
        }
        else {
            if (mEmailError.getVisibility() == View.VISIBLE) {
                mEmailError.setVisibility(View.INVISIBLE);
                mEmailError.setText("");
                mEmailEditText.getBackground().setColorFilter(getResources().getColor(R.color.material_grey_800), PorterDuff.Mode.SRC_IN);
            }
        }
        return true;
    }

    private void checkAllFields() {
        if (checkUsernameField() && checkPasswordField() && checkConfirmPasswordField() && checkEmailField()) {
            Log.i(LOG_TAG, "Ready to sign up");
            new CheckUsernameAvailability().execute(mUsernameEditText.getText().toString());
        }
        else {
            if (!checkUsernameField()) {
                //intentionally blank
            }
            if (!checkPasswordField()) {
                //intentionally blank
            }
            if (!checkConfirmPasswordField()) {
                //intentionally blank
            }
            if (!checkEmailField()) {
                //intentionally blank
            }
        }
    }
    /*****************************************END OF CHECK METHODS*************************************/

    /*****************************************START OF DATABASE METHODS*************************************/
    //used to check if the input username is already registered
    private class CheckUsernameAvailability extends AsyncTask<String, Void, Cursor> {
        @Override
        protected Cursor doInBackground(String... params) {
            String inputUsername = params[0];
            final String[] USERS_COLUMNS = {
                    UsersContract.UsersEntry.TABLE_NAME + "." + UsersContract.UsersEntry._ID,
                    UsersContract.UsersEntry.COLUMN_USERNAME
            };
            String[] querySelectionArgs = {inputUsername.toLowerCase()};
            Log.i(LOG_TAG, "queryArgs[0]: " + querySelectionArgs[0]);
            String querySelection = "LOWER(" + UsersContract.UsersEntry.COLUMN_USERNAME + ")" + " = ?";
            return mContext.getContentResolver().query(
                    UsersContract.UsersEntry.CONTENT_URI,
                    USERS_COLUMNS,
                    querySelection,
                    querySelectionArgs,
                    null
            );
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            int cursorCount = cursor.getCount();
            Log.i(LOG_TAG,"cursorCount: " + cursorCount);
            if (cursorCount == 0) {
                //Log.i(LOG_TAG, "The user does not exist, proceed further");
                //save the new user
                Vector<ContentValues> cVVector = new Vector<>(3);
                ContentValues userValues = new ContentValues();
                userValues.put(UsersContract.UsersEntry.COLUMN_USERNAME, mUsernameEditText.getText().toString());
                //Log.i(LOG_TAG, "Encoded password: " + Utility.generateEncodedPassword(mPasswordEditText.getText().toString()));
                userValues.put(UsersContract.UsersEntry.COLUMN_PASSWORD, Utility.generateEncodedPassword(mPasswordEditText.getText().toString()));
                userValues.put(UsersContract.UsersEntry.COLUMN_USER_TYPE, Constants.ADMIN_USER_TYPE);
                userValues.put(UsersContract.UsersEntry.COLUMN_EMAIL, mEmailEditText.getText().toString());
                cVVector.add(userValues);
                new SaveUsernameAsync().execute(cVVector);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminSignUpActivity.this);
                builder.setMessage(R.string.signup_user_exists)
                        .setTitle(R.string.generic_error_occurred)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    class SaveUsernameAsync extends AsyncTask<Vector<ContentValues>, Void, Integer> {
        @Override
        protected Integer doInBackground(Vector<ContentValues>... params) {
            Vector<ContentValues> contentValuesVector = params[0];
            ContentValues[] cvArray = new ContentValues[contentValuesVector.size()];
            contentValuesVector.toArray(cvArray);
            return mContext.getContentResolver().bulkInsert(UsersContract.UsersEntry.CONTENT_URI, cvArray);
        }

        @Override
        protected void onPostExecute(Integer rowsInserted) {
            Log.i(LOG_TAG, "rowsInserted: " + rowsInserted);
            if (rowsInserted == 1) {
                //save the login
                mSecurePreferences.put(Constants.PREF_IS_ADMIN_LOGGED_IN_KEY, Constants.PREF_IS_ADMIN_LOGGED_IN_TRUE);
                //open the main activity
                Intent mainActivityIntent = new Intent(AdminSignUpActivity.this, AdminMainActivity.class);
                //clear the intent stack so that the user can't return to this activity
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivityIntent);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminSignUpActivity.this);
                builder.setMessage(R.string.signup_user_failed)
                        .setTitle(R.string.generic_error_occurred)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }
    }

    /*****************************************END OF DATABASE METHODS*************************************/
}
