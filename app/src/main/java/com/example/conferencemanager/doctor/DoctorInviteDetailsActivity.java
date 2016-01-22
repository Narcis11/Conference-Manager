package com.example.conferencemanager.doctor;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.conferencemanager.R;
import com.example.conferencemanager.utilities.Constants;

public class DoctorInviteDetailsActivity extends AppCompatActivity {

    private static final String LOG_TAG = DoctorInviteDetailsActivity.class.getSimpleName();
    private Bundle mBundle;
    private Context mContext;
    //the titles
    private TextView mTitleTextView;
    private TextView mAddressTextView;
    private TextView mDescriptionTextView;
    private TextView mDateTextView;
    //the values
    private EditText mTitleEditText;
    private EditText mAddressEditText;
    private EditText mDescriptionEditText;
    private TextView mDateEditText;
    //the buttons
    private Button mAcceptButton;
    private Button mDeclineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity_invite_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBundle = getIntent().getExtras();
        mContext = getApplicationContext();
        String actionBarTitle = mBundle.getString(Constants.BUNDLE_DOCTOR_INVITE_TITLE_KEY);
        getSupportActionBar().setTitle(actionBarTitle);
        loadUiElements();
    }

    /**********************************************************START OF UI METHODS****************************************************************/

    private void loadUiElements() {
        mTitleTextView = (TextView) findViewById(R.id.doctor_invite_details_title);
        mAddressTextView = (TextView) findViewById(R.id.doctor_invite_details_address);
        mDescriptionTextView = (TextView) findViewById(R.id.doctor_invite_details_description);
        mDateTextView = (TextView) findViewById(R.id.doctor_invite_details_date);
        //the edittexts
        mTitleEditText = (EditText) findViewById(R.id.doctor_invite_details_title_value);
        mAddressEditText = (EditText) findViewById(R.id.doctor_invite_details_address_value);
        mDescriptionEditText = (EditText) findViewById(R.id.doctor_invite_details_description_value);
        mDateEditText = (TextView) findViewById(R.id.doctor_invite_details_date_value);
        //set the texts
        mTitleEditText.setText(mBundle.getString(Constants.BUNDLE_DOCTOR_INVITE_TITLE_KEY));
        mAddressEditText.setText(mBundle.getString(Constants.BUNDLE_DOCTOR_INVITE_ADDRESS_KEY));
        mDescriptionEditText.setText(mBundle.getString(Constants.BUNDLE_DOCTOR_INVITE_DESCRIPTION_KEY));
        mDateEditText.setText(mBundle.getString(Constants.BUNDLE_DOCTOR_INVITE_DATE_KEY));

    }
    /**********************************************************END OF UI METHODS****************************************************************/

}
