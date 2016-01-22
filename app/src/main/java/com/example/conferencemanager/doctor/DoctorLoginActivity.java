package com.example.conferencemanager.doctor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.conferencemanager.R;
import com.example.conferencemanager.admin.AdminSignUpActivity;
import com.example.conferencemanager.data.UsersContract;
import com.example.conferencemanager.utilities.Constants;
import com.example.conferencemanager.utilities.SecurePreferences;
import com.example.conferencemanager.utilities.Utility;

public class DoctorLoginActivity extends AppCompatActivity {

    private Context mContext;
    //the edit texts
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    //the button used
    private Button mLogInButton;
    private Button mSignUpButton;
    //the error textviews
    private TextView mUsernameError;
    private TextView mPasswordError;
    //generic error message
    private String EMPTY_FIELD_ERROR = "";
    //test user and password
    private SecurePreferences mSecurePreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = getApplicationContext();
        mSecurePreferences = new SecurePreferences(mContext, Constants.PREF_CREDENTIALS, Constants.PREF_CREDENTIALS_KEY, true);
        EMPTY_FIELD_ERROR = getResources().getString(R.string.no_input);
        loadUiElements();
        setOnClickListeners();
    }

    /******************************* START OF UI METHODS******************************/
    private void loadUiElements() {
        //the edittext views
        mUsernameEditText = (EditText) findViewById(R.id.doctor_login_username_value);
        mPasswordEditText = (EditText) findViewById(R.id.doctor_login_password_value);
        //the buttons
        mLogInButton = (Button) findViewById(R.id.doctor_login_button);
        mSignUpButton = (Button) findViewById(R.id.doctor_login_signup_button);
        //the error fields
        mUsernameError = (TextView) findViewById(R.id.doctor_login_username_error);
        mPasswordError = (TextView) findViewById(R.id.doctor_login_password_error);
    }
    /*******************************END OF UI METHODS******************************/

    /****************************START OF onClick/NAVIGATION METHODS****************************/
    private void setOnClickListeners() {
        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDoctorCredentials();
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignUpActivity();
            }
        });

    }

    private void openSignUpActivity() {
        Intent buyerIntent = new Intent(this, DoctorSignUpActivity.class);
        startActivity(buyerIntent);
    }
    /****************************END OF onClick/NAVIGATION METHODS****************************/

    /*****************************START OF CHECK METHODS*****************************/

    private boolean checkUsernameField() {
        if (mUsernameEditText.getText().toString().length() == 0) {
            mUsernameError.setVisibility(View.VISIBLE);
            mUsernameError.setText(EMPTY_FIELD_ERROR);
            mUsernameEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            //cancelLogInDialog();
            return false;
        }
        else {
            if (mUsernameError.getVisibility() == View.VISIBLE) {
                mUsernameError.setVisibility(View.INVISIBLE);
                mUsernameError.setText("");
                mUsernameEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
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
            //cancelLogInDialog();
            return false;
        }
        else {
            if (mPasswordError.getVisibility() == View.VISIBLE) {
                mPasswordError.setVisibility(View.INVISIBLE);
                mPasswordError.setText("");
                mPasswordEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
                return true;
            }
        }
        return true;
    }

    private void checkDoctorCredentials() {
        if (checkUsernameField() && checkPasswordField()) {
            String[] userValues = new String[2];
            userValues[0] = mUsernameEditText.getText().toString();
            userValues[1] = mPasswordEditText.getText().toString();
            new CheckUserCredentialsAsync().execute(userValues);

        }
        else {
            if (!checkUsernameField()) {
                //intentionally blank
            }
            if (!checkPasswordField()) {
                //intentionally blank
            }
        }
    }
    /*****************************END OF CHECK METHODS*****************************/

    /*****************************************START OF DATABASE METHODS*************************************/
    class CheckUserCredentialsAsync extends AsyncTask<String, Void, Cursor> {
        @Override
        protected Cursor doInBackground(String... params) {
            String inputUsername = params[0];
            String inputPassword = params[1];
            String[] querySelectionArgs = {inputUsername, Utility.generateEncodedPassword(inputPassword), Constants.DOCTOR_USER_TYPE};
            String querySelection = UsersContract.UsersEntry.COLUMN_USERNAME + " = ? AND " +
                    UsersContract.UsersEntry.COLUMN_PASSWORD + " = ? AND " + UsersContract.UsersEntry.COLUMN_USER_TYPE + " = ?";
            final String[] USERS_COLUMNS = {
                    UsersContract.UsersEntry.TABLE_NAME + "." + UsersContract.UsersEntry._ID,
                    UsersContract.UsersEntry.COLUMN_USERNAME,
                    UsersContract.UsersEntry.COLUMN_PASSWORD
            };
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
            if (cursorCount == 1) {
                //save the login
                mSecurePreferences.put(Constants.PREF_IS_DOCTOR_LOGGED_IN_KEY, Constants.PREF_IS_DOCTOR_LOGGED_IN_TRUE);
                //save the doctor's username
                mSecurePreferences.put(Constants.PREF_DOCTOR_USERNAME_KEY, mUsernameEditText.getText().toString());
                //open the main activity
                Intent mainActivityIntent = new Intent(DoctorLoginActivity.this, DoctorMainActivity.class);
                //clear the intent stack so that the user can't return to this activity
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivityIntent);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorLoginActivity.this);
                builder.setMessage(R.string.invalid_login)
                        .setTitle(R.string.generic_error_occurred)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }

        }
    }
    /*****************************************END OF DATABASE METHODS*************************************/

}
