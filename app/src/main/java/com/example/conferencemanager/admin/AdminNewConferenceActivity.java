package com.example.conferencemanager.admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.conferencemanager.R;
import com.example.conferencemanager.data.UsersContract;
import com.example.conferencemanager.utilities.Constants;
import com.example.conferencemanager.utilities.SecurePreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

public class AdminNewConferenceActivity extends AppCompatActivity {

    private final String LOG_TAG = AdminNewConferenceActivity.class.getSimpleName();
    private Context mContext;
    //the edittext views
    private EditText mTitleValue;
    private EditText mAddressValue;
    private EditText mDescriptionValue;
    private TextView mDateValue;
    private CheckBox mInviteValue;
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
    //used to determine whether the invite checkbox is ticked
    private final String IS_INVITES_CHECKED = "INVITES_CHECKED";
    //used to get the admin's username
    private SecurePreferences mSecurePreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_new_conference);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = getApplicationContext();
        mSecurePreferences = new SecurePreferences(mContext, Constants.PREF_CREDENTIALS, Constants.PREF_CREDENTIALS_KEY, true);
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
        mInviteValue = (CheckBox) findViewById(R.id.admin_new_conf_invites_value);
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
        //Log.i(LOG_TAG, "In updateConfCalendar");
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
            String[] confValues = new String[5];
            confValues[0] = mTitleValue.getText().toString();
            confValues[1] = mDescriptionValue.getText().toString();
            confValues[2] = mAddressValue.getText().toString();
            confValues[3] = mDateValue.getText().toString();
            confValues[4] = mInviteValue.isChecked() ? IS_INVITES_CHECKED : "";
            new SaveNewConferenceAsync().execute(confValues);
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
                    //Log.i(LOG_TAG,"Adding doctor: " + cursor.getString(COL_DOCTOR_USERNAME));
                    mDoctorsUsernamesList.add(cursor.getString(COL_DOCTOR_USERNAME));
                }
            }
        }
    }

    private class SaveNewConferenceAsync extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            Vector<ContentValues> cVVector = new Vector<>(1);
            ContentValues confValues = new ContentValues();
            final String adminUsername = mSecurePreferences.getString(Constants.PREF_ADMIN_USERNAME_KEY);

            confValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_TITLE, params[0]);
            confValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_DESCRIPTION, params[1]);
            confValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_ADDRESS, params[2]);
            confValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_DATE, params[3]);
            confValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_ADDED_BY, adminUsername);

            cVVector.add(confValues);
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            int rowsInserted = mContext.getContentResolver().bulkInsert(UsersContract.ConferencesEntry.CONTENT_URI, cvArray);
            //Log.i(LOG_TAG,"No of rows inserted in the conferences table: " + rowsInserted);
            //insert the invites if necessary
            if (params[4].equals(IS_INVITES_CHECKED)) {
                Vector<ContentValues> invitesVector = new Vector<>(mDoctorsUsernamesList.size());
                for (int x = 0; x < mDoctorsUsernamesList.size(); x++) {
                    ContentValues invitesValues = new ContentValues();
                    invitesValues.put(UsersContract.InvitesEntry.COLUMN_CONF_NAME, params[0]);
                    invitesValues.put(UsersContract.InvitesEntry.COLUMN_RECIPIENT, mDoctorsUsernamesList.get(x));
                    invitesValues.put(UsersContract.InvitesEntry.COLUMN_SENDER, adminUsername);
                    invitesVector.add(invitesValues);
                }
                if (invitesVector.size() > 0) {
                    ContentValues[] cvInvitesArray = new ContentValues[invitesVector.size()];
                    invitesVector.toArray(cvInvitesArray);
                    int invitesInserted = mContext.getContentResolver().bulkInsert(UsersContract.InvitesEntry.CONTENT_URI, cvInvitesArray);
                    //Log.i(LOG_TAG,"invitesInserted: " + invitesInserted);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //open the main activity
            Intent mainActivityIntent = new Intent(AdminNewConferenceActivity.this, AdminMainActivity.class);
            //clear the intent stack so that the user can't return to this activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainActivityIntent);
        }
    }
    /*****************************************END OF ASYNC METHODS*************************************/

}
