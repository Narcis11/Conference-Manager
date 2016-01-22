package com.example.conferencemanager.admin;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.conferencemanager.R;

public class AdminNewConferenceActivity extends AppCompatActivity {

    private final String LOG_TAG = AdminNewConferenceActivity.class.getSimpleName();
    private Context mContext;
    //the edittext views
    private EditText mTitleValue;
    private EditText mAddressValue;
    private EditText mDescriptionValue;
    private TextView mDateValue;
    //the error messages
    private TextView mTitleError;
    private TextView mAddressError;
    private TextView mDescriptionError;
    private TextView mDateError;
    //the add conference button
    private Button mAddConferenceButton;
    //generic error message
    private static String EMPTY_FIELD_ERROR = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_new_conference);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = getApplicationContext();
        EMPTY_FIELD_ERROR = getResources().getString(R.string.no_input);
        loadUIElements();
    }

    /**************************************START OF UI METHODS*****************************************/
    private void loadUIElements() {
        //the edittext views
        mTitleValue = (EditText) findViewById(R.id.admin_new_conf_title_value);
        mAddressValue = (EditText) findViewById(R.id.admin_new_conf_address_value);
        mDescriptionValue = (EditText) findViewById(R.id.admin_new_conf_description_value);
        mDateValue = (TextView) findViewById(R.id.admin_new_conf_date_value);
        //the error textviews
        mTitleError = (TextView) findViewById(R.id.admin_new_conf_title_error);
        mAddressError = (TextView) findViewById(R.id.admin_new_conf_address_error);
        mDescriptionError = (TextView) findViewById(R.id.admin_new_conf_description_error);
        mDateError = (TextView) findViewById(R.id.admin_new_conf_date_error);
        //the add conference button
        mAddConferenceButton = (Button) findViewById(R.id.admin_new_conf_button);
    }

    /**************************************END OF UI METHODS*****************************************/

}
