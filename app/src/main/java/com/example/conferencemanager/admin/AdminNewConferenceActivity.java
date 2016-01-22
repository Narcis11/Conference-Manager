package com.example.conferencemanager.admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.conferencemanager.R;
import com.example.conferencemanager.data.UsersContract;
import com.example.conferencemanager.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    //the list that stored the doctors' usernames
    private ArrayList<String> mDoctorsUsernamesList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_new_conference);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = getApplicationContext();
        EMPTY_FIELD_ERROR = getResources().getString(R.string.no_input);
        loadUIElements();
        setOnClickListeners();
        //get the list of doctors
        new GetDoctorsListAsync().execute();
    }

    /**************************************START OF UI/onClick METHODS*****************************************/
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

    private void setOnClickListeners() {
        mAddConferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllFields();
            }
        });

        mDateValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar conferenceCalendar = Calendar.getInstance();

                final TimePickerDialog.OnTimeSetListener timePickerDialog = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        conferenceCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        conferenceCalendar.set(Calendar.MINUTE, minute);
                        updateConfCalendar(conferenceCalendar);
                    }
                };

                final DatePickerDialog.OnDateSetListener datePickerDialog = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        conferenceCalendar.set(Calendar.YEAR, year);
                        conferenceCalendar.set(Calendar.MONTH, monthOfYear);
                        conferenceCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        //after the date is set, launch the dialog to set the hour and minute
                        new TimePickerDialog(AdminNewConferenceActivity.this, timePickerDialog, conferenceCalendar.get(Calendar.HOUR_OF_DAY),
                                conferenceCalendar.get(Calendar.MINUTE), true).show();
                    }
                };

                new DatePickerDialog(AdminNewConferenceActivity.this, datePickerDialog, conferenceCalendar
                        .get(Calendar.YEAR), conferenceCalendar.get(Calendar.MONTH),
                        conferenceCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void updateConfCalendar(Calendar calendar) {
        Log.i(LOG_TAG, "In updateConfCalendar");
        String dateFormat = "HH:mm, MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);
        mDateValue.setText(sdf.format(calendar.getTime()));
    }

    /**************************************END OF UI/onClick METHODS*****************************************/

    /*****************************************START OF CHECK METHODS*************************************/

    private boolean checkTitleField() {
        if (mTitleValue.getText().toString().length() == 0) {
            mTitleError.setVisibility(View.VISIBLE);
            mTitleError.setText(EMPTY_FIELD_ERROR);
            mTitleValue.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            return false;
        }
        else {
            if (mTitleError.getVisibility() == View.VISIBLE) {
                mTitleError.setVisibility(View.INVISIBLE);
                mTitleError.setText("");
                mTitleValue.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.material_grey_800_local), PorterDuff.Mode.SRC_IN);
                return true;
            }
        }
        return true;
    }

    private boolean checkDescriptionField() {
        if (mDescriptionValue.getText().toString().length() == 0) {
            mDescriptionError.setVisibility(View.VISIBLE);
            mDescriptionError.setText(EMPTY_FIELD_ERROR);
            mDescriptionValue.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            return false;
        }
        else {
            if (mDescriptionError.getVisibility() == View.VISIBLE) {
                mDescriptionError.setVisibility(View.INVISIBLE);
                mDescriptionError.setText("");
                mDescriptionValue.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.material_grey_800_local), PorterDuff.Mode.SRC_IN);
                return true;
            }
        }
        return true;
    }

    private boolean checkAddressField() {
        if (mAddressValue.getText().toString().length() == 0) {
            mAddressError.setVisibility(View.VISIBLE);
            mAddressError.setText(EMPTY_FIELD_ERROR);
            mAddressValue.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            return false;
        }
        else {
            if (mAddressError.getVisibility() == View.VISIBLE) {
                mAddressError.setVisibility(View.INVISIBLE);
                mAddressError.setText("");
                mAddressValue.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.material_grey_800_local), PorterDuff.Mode.SRC_IN);
                return true;
            }
        }
        return true;
    }

    private boolean checkDateField() {
        if (mDateValue.getText().toString().length() == 0) {
            mDateError.setVisibility(View.VISIBLE);
            mDateError.setText(EMPTY_FIELD_ERROR);
            mDateValue.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
            return false;
        }
        else {
            if (mDateError.getVisibility() == View.VISIBLE) {
                mDateError.setVisibility(View.INVISIBLE);
                mDateError.setText("");
                mDateValue.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.material_grey_800_local), PorterDuff.Mode.SRC_IN);
                return true;
            }
        }
        return true;
    }

    private void checkAllFields() {
        if (checkTitleField() && checkDescriptionField() && checkAddressField() && checkDateField()) {
            Log.i(LOG_TAG,"Ready to save the conference");
            //String[]
        }
        else {
            if (!checkTitleField()) {
                //intentionally blank
            }
            if (!checkAddressField()) {
                //intentionally blank
            }
            if (!checkDescriptionField()) {
                //intentionally blank
            }
            if (!checkDateField()) {
                //intentionally blank
            }
        }
    }
    /*****************************************END OF CHECK METHODS*************************************/


    /*****************************************START OF ASYNC METHODS*************************************/
    private class GetDoctorsListAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected Cursor doInBackground(Void... params) {
            final String[] DOCTORS_COLUMNS = {
                    UsersContract.UsersEntry.TABLE_NAME + "." + UsersContract.UsersEntry._ID,
                    UsersContract.UsersEntry.COLUMN_USERNAME
            };
            final String querySelection = UsersContract.UsersEntry.COLUMN_USER_TYPE + " = ?";
            final String[] querySelectionArgs = {Constants.DOCTOR_USER_TYPE};
            return mContext.getContentResolver().query(
                    UsersContract.UsersEntry.CONTENT_URI,
                    DOCTORS_COLUMNS,
                    querySelection,
                    querySelectionArgs,
                    null
            );
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            final int COL_DOCTOR_USERNAME = 1;
            if (cursor != null && cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    Log.i(LOG_TAG,"Addind doctor: " + cursor.getString(COL_DOCTOR_USERNAME));
                    mDoctorsUsernamesList.add(cursor.getString(COL_DOCTOR_USERNAME));
                }
            }
        }
    }

    /*****************************************END OF ASYNC METHODS*************************************/

}
