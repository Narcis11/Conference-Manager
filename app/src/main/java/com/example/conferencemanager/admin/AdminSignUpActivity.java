package com.example.conferencemanager.admin;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.conferencemanager.R;

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
    //the error messages
    private TextView mUsernameError;
    private TextView mPasswordError;
    private TextView mConfirmPasswordError;
    private TextView mEmailError;
    //the next button
    private Button mSignUpButton;
    //generic error message
    private static String EMPTY_FIELD_ERROR = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = getApplicationContext();
        EMPTY_FIELD_ERROR = getResources().getString(R.string.no_input);
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
            mPasswordError.setText(getResources().getString(R.string.admin_signup_short_password));
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
            mConfirmPasswordError.setText(getResources().getString(R.string.admin_signup_password_mismatch));
            mConfirmPasswordEditText.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            return false;
        }
        else {
            if (mConfirmPasswordError.getVisibility() == View.VISIBLE) {
                mConfirmPasswordError.setVisibility(View.INVISIBLE);
                mConfirmPasswordError.setText("");
                mConfirmPasswordEditText.getBackground().setColorFilter(getResources().getColor(R.color.material_grey_800), PorterDuff.Mode.SRC_IN);
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
            mEmailError.setText(getResources().getString(R.string.admin_registration_invalid_email));
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
            Log.i(LOG_TAG,"Ready to sign up");
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
    /*****************************************START OF CHECK METHODS*************************************/

}
