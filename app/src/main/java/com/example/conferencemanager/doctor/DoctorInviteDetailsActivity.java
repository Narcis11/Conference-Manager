package com.example.conferencemanager.doctor;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.conferencemanager.R;
import com.example.conferencemanager.data.UsersContract;
import com.example.conferencemanager.utilities.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

public class DoctorInviteDetailsActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

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
    //the code when requesting the right to access the calendar
    final int CALENDAR_ACCESS_CODE = 99;


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
        setOnClickListeners();
    }

    /**********************************************************START OF UI/onClick METHODS****************************************************************/

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
        //the buttons
        mAcceptButton = (Button) findViewById(R.id.doctor_invite_details_accept);
        mDeclineButton = (Button) findViewById(R.id.doctor_invite_details_decline);
        //set the texts
        mTitleEditText.setText(mBundle.getString(Constants.BUNDLE_DOCTOR_INVITE_TITLE_KEY));
        mAddressEditText.setText(mBundle.getString(Constants.BUNDLE_DOCTOR_INVITE_ADDRESS_KEY));
        mDescriptionEditText.setText(mBundle.getString(Constants.BUNDLE_DOCTOR_INVITE_DESCRIPTION_KEY));
        mDateEditText.setText(mBundle.getString(Constants.BUNDLE_DOCTOR_INVITE_DATE_KEY));
    }

    private void setOnClickListeners() {
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptInvitation();
            }
        });
        mDeclineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineInvitation();
            }
        });
    }
    /**********************************************************END OF UI/onClick METHODS****************************************************************/

    /********************************************************START OF ACCEPT/DECLINE METHODS*************************************************************/
    private void acceptInvitation() {
        //TODO: add the invitation to the doctor's calendar
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_CALENDAR}, CALENDAR_ACCESS_CODE);
        }
        else {
            String[] calendarValues = new String[4];
            calendarValues[0] = mTitleEditText.getText().toString();
            calendarValues[1] = mDescriptionEditText.getText().toString();
            calendarValues[2] = mAddressEditText.getText().toString();
            calendarValues[3] = mDateEditText.getText().toString();
            new SaveToCalendarAsync().execute(calendarValues);
        }
        //TODO: delete the invitation for this doctor
        //TODO: open the main activity
    }

    private void declineInvitation() {
        //TODO: delete the invitation for this doctor
        //TODO: open the main activity
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(LOG_TAG, "In onRequestPermissionsResult, request/result: " + requestCode + "/" + grantResults[0]);
        if (requestCode == CALENDAR_ACCESS_CODE) {
            
        }
    }

    /********************************************************END OF ACCEPT/DECLINE METHODS*************************************************************/

    /********************************************************START OF ASYNC METHODS*************************************************************/

    private class SaveToCalendarAsync extends AsyncTask<String, Void, Integer>{
        final int CALENDAR_WRITE_DENIED = 101;
        @Override
        protected Integer doInBackground(String... params) {
            Vector<ContentValues> cVVector = new Vector<>(1);
            ContentValues calendarValues = new ContentValues();

            calendarValues.put(CalendarContract.Events.TITLE, params[0]);
            calendarValues.put(CalendarContract.Events.DESCRIPTION, params[1]);
            calendarValues.put(CalendarContract.Events.HAS_ALARM,1);
            calendarValues.put(CalendarContract.Events.CALENDAR_ID, 1);
            //set the time zone
            TimeZone timeZone = TimeZone.getDefault();
            calendarValues.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
            //for this version of the app, we set the duration to a default of 12h
            calendarValues.put(CalendarContract.Events.DURATION, "+P12H");
            try {
                //format the date
                String dateFormat = "HH:mm, MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
                Date date = (Date) sdf.parse(params[3]);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendarValues.put(CalendarContract.Events.DTSTART, calendar.getTimeInMillis());
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
            cVVector.add(calendarValues);
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED)
                return mContext.getContentResolver().bulkInsert(CalendarContract.Events.CONTENT_URI, cvArray);
            else
                return CALENDAR_WRITE_DENIED;
        }

        @Override
        protected void onPostExecute(Integer insertResult) {
            if (insertResult == 1) {
                Log.i(LOG_TAG,"Calendar insert successful");
                //even though the event was not added to the calendar, we still continue with the acceptance procedure
            }
            else if (insertResult == CALENDAR_WRITE_DENIED) {
                Log.i(LOG_TAG,"Calendar insert DENIED");
            }
        }
    }
    /********************************************************END OF ASYNC METHODS*************************************************************/
}
