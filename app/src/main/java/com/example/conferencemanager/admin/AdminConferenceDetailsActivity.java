package com.example.conferencemanager.admin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.conferencemanager.R;
import com.example.conferencemanager.data.UsersContract;
import com.example.conferencemanager.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;

public class AdminConferenceDetailsActivity extends AppCompatActivity{

    private static final String LOG_TAG = AdminConferenceDetailsActivity.class.getSimpleName();
    private Bundle mBundle;
    private Menu mMenu;
    private Context mContext;
    //used to determine whether the edit button has been pressed
    private boolean mIsEditModeEnabled;
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
    //the initial edittexts values
    private String mTitleInitialValue;
    private String mAddressInitialValue;
    private String mDescriptionInitialValue;
    private String mDateInitialValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_conference_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBundle = getIntent().getExtras();
        mContext = getApplicationContext();
        String actionBarTitle = mBundle.getString(Constants.BUNDLE_ADMIN_CONF_TITLE_KEY);
        getSupportActionBar().setTitle(actionBarTitle);
        loadUiElements();
    }

    /**********************************************************START OF UI METHODS****************************************************************/
    private void loadUiElements() {
        mTitleTextView = (TextView) findViewById(R.id.admin_conf_details_title);
        mAddressTextView = (TextView) findViewById(R.id.admin_conf_details_address);
        mDescriptionTextView = (TextView) findViewById(R.id.admin_conf_details_description);
        mDateTextView = (TextView) findViewById(R.id.admin_conf_details_date);
        //the edittexts
        mTitleEditText = (EditText) findViewById(R.id.admin_conf_details_title_value);
        mAddressEditText = (EditText) findViewById(R.id.admin_conf_details_address_value);
        mDescriptionEditText = (EditText) findViewById(R.id.admin_conf_details_description_value);
        mDateEditText = (TextView) findViewById(R.id.admin_conf_details_date_value);
        //set the texts
        mTitleEditText.setText(mBundle.getString(Constants.BUNDLE_ADMIN_CONF_TITLE_KEY));
        mAddressEditText.setText(mBundle.getString(Constants.BUNDLE_ADMIN_CONF_ADDRESS_KEY));
        mDescriptionEditText.setText(mBundle.getString(Constants.BUNDLE_ADMIN_CONF_DESCRIPTION_KEY));
        mDateEditText.setText(mBundle.getString(Constants.BUNDLE_ADMIN_CONF_DATE_KEY));
        //save the edittexts initial values
        mTitleInitialValue = mTitleEditText.getText().toString();
        mAddressInitialValue = mAddressEditText.getText().toString();
        mDescriptionInitialValue = mDescriptionEditText.getText().toString();
        mDateInitialValue = mDateEditText.getText().toString();

    }
    /**********************************************************END OF UI METHODS****************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.admin_menu_conf_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.i(LOG_TAG, "In onOptionsItemSelected");
        if (id == R.id.action_edit_conf) {
            Log.i(LOG_TAG, "Clicked on the edit button");
            if (!mIsEditModeEnabled) {
                enableEditableMode();
            }
            /*REMOVE*/
            else {//we are in editable mode, and the "remove" button is pressed
                onRemoveButtonPressed();
            }
        }
        else if (id == R.id.action_delete_conf) {
            Log.i(LOG_TAG,"Clicked on the delete button");
            /*ACCEPT*/
            if (mIsEditModeEnabled) {//we are in editable mode, and the "accept" button is pressed
                onAcceptButtonPressed();
            }
            else {//the delete button was pressed
                deleteConference();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    /**********************************************************START OF EDIT/DELETE METHODS****************************************************************/
    private void enableEditableMode() {
        Log.i(LOG_TAG, "In enableEditableMode");
        //enable all the edittexts
        mTitleEditText.setEnabled(true);
        mAddressEditText.setEnabled(true);
        mDescriptionEditText.setEnabled(true);
        mDateEditText.setEnabled(true);
        //in addition, the date edittext also has to be clickable to trigger the calendar display
        //mDateEditText.setClickable(true);
        //change the icons
        mMenu.getItem(0).setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_action_remove));//the left icon
        mMenu.getItem(1).setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_action_accept));//the right icon
        //set the logic for the date field
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
                new TimePickerDialog(AdminConferenceDetailsActivity.this, timePickerDialog, conferenceCalendar.get(Calendar.HOUR_OF_DAY),
                        conferenceCalendar.get(Calendar.MINUTE), true).show();
            }
        };

        mDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "In mDateEditText onClickListener");
                new DatePickerDialog(AdminConferenceDetailsActivity.this, datePickerDialog, conferenceCalendar
                        .get(Calendar.YEAR), conferenceCalendar.get(Calendar.MONTH),
                        conferenceCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //the mode has changed
        mIsEditModeEnabled = true;
    }

    private void onRemoveButtonPressed() {
        mMenu.getItem(0).setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_action_edit));//the left icon
        mMenu.getItem(1).setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_action_delete));//the right icon
        //set the edittexts back to their initial values
        mTitleEditText.setText(mTitleInitialValue);
        mAddressEditText.setText(mAddressInitialValue);
        mDescriptionEditText.setText(mDescriptionInitialValue);
        mDateEditText.setText(mDateInitialValue);
        //disable the edittexts
        mTitleEditText.setEnabled(false);
        mAddressEditText.setEnabled(false);
        mDescriptionEditText.setEnabled(false);
        mDateEditText.setEnabled(false);

        mIsEditModeEnabled = false;
    }

    private void onAcceptButtonPressed() {
        Log.i(LOG_TAG, "Ready to save the changes");
        //change the icons back
        mMenu.getItem(0).setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_action_edit));//the left icon
        mMenu.getItem(1).setIcon(ContextCompat.getDrawable(mContext, R.drawable.ic_action_delete));//the right icon
        //set the new edittexts values
        mTitleEditText.setText(mTitleEditText.getText().toString());
        mAddressEditText.setText(mAddressEditText.getText().toString());
        mDescriptionEditText.setText(mDescriptionEditText.getText().toString());
        mDateEditText.setText(mDateEditText.getText().toString());
        //disable the edittexts
        mTitleEditText.setEnabled(false);
        mAddressEditText.setEnabled(false);
        mDescriptionEditText.setEnabled(false);
        mDateEditText.setEnabled(false);

        mIsEditModeEnabled = false;
        //save to the database
        String[] updateArray = new String[4];
        updateArray[0] = mTitleEditText.getText().toString();
        updateArray[1] = mDescriptionTextView.getText().toString();
        updateArray[2] = mAddressEditText.getText().toString();
        updateArray[3] = mDateEditText.getText().toString();
        new SaveConferenceDetailsAsync().execute(updateArray);
    }

    private void updateConfCalendar(Calendar calendar) {
        Log.i(LOG_TAG,"In updateConfCalendar");
        String dateFormat = "HH:mm, MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        mDateEditText.setText(sdf.format(calendar.getTime()));
    }

    private void deleteConference() {
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminConferenceDetailsActivity.this);
        builder.setMessage(R.string.admin_delete_conf_dialog_body)
                .setTitle(R.string.admin_delete_conf_confirm)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG,"Ready to delete the conference");
                dialog.cancel();
                new DeleteConferenceAsync().execute();
            }
        });
    }
    /**********************************************************END OF EDIT/DELETE METHODS****************************************************************/


    /**********************************************************START OF ASYNC METHODS****************************************************************/
    private class SaveConferenceDetailsAsync extends AsyncTask<String, Void, Integer> {
        final int NO_ROWS_MODIFIED = 101;
        @Override
        protected Integer doInBackground(String... params) {
            Vector<ContentValues> cVVector = new Vector<>(1);
            ContentValues updateValues = new ContentValues();

            if (!params[0].equals(mTitleInitialValue))
                updateValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_TITLE, params[0]);
            if (!params[1].equals(mDescriptionInitialValue))
                updateValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_DESCRIPTION, params[1]);
            if (!params[2].equals(mAddressInitialValue))
                updateValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_ADDRESS, params[2]);
            if (!params[3].equals(mDateInitialValue))
                updateValues.put(UsersContract.ConferencesEntry.COLUMN_CONF_DATE, params[3]);

            cVVector.add(updateValues);
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            Log.i(LOG_TAG,"updateValues.size(): " + updateValues.size());
            if (updateValues.size() > 0) {
                cVVector.toArray(cvArray);
                String querySelection = UsersContract.ConferencesEntry._ID + " = ?";
                String[] querySelectionArgs = {String.valueOf(mBundle.getInt(Constants.BUNDLE_ADMIN_CONF_ID_KEY))};
                return mContext.getContentResolver().update(UsersContract.ConferencesEntry.CONTENT_URI, cvArray[0], querySelection, querySelectionArgs);
            }
            return NO_ROWS_MODIFIED;
        }

        @Override
        protected void onPostExecute(Integer rowsUpdated) {
            if (rowsUpdated == 0) {
                //set the edittexts back to their initial value
                mTitleEditText.setText(mTitleInitialValue);
                mAddressEditText.setText(mAddressInitialValue);
                mDescriptionEditText.setText(mDescriptionInitialValue);
                mDateEditText.setText(mDateInitialValue);
                //inform the user of the error
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminConferenceDetailsActivity.this);
                builder.setMessage(R.string.admin_edit_conf_error)
                        .setTitle(R.string.generic_error_occurred)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    private class DeleteConferenceAsync extends AsyncTask<Void, Void, Integer> {
        final int DELETE_OK = 99;
        final int DELETE_NOK = 89;
        @Override
        protected Integer doInBackground(Void... params) {
            String querySelectionConf = UsersContract.ConferencesEntry._ID + " = ?";
            String[] querySelectionConfArgs = {String.valueOf(mBundle.getInt(Constants.BUNDLE_ADMIN_CONF_ID_KEY))};
            //we first delete from the invites table
            String querySelectionInvites = UsersContract.InvitesEntry.COLUMN_CONF_NAME + " = ?";
            String[] querySelectionInvitesArgs = {mBundle.getString(Constants.BUNDLE_ADMIN_CONF_TITLE_KEY)};
            int noInvitesDeleted = mContext.getContentResolver().delete(UsersContract.InvitesEntry.CONTENT_URI, querySelectionInvites, querySelectionInvitesArgs);
            int noConfsDeleted = mContext.getContentResolver().delete(UsersContract.ConferencesEntry.CONTENT_URI, querySelectionConf, querySelectionConfArgs);
            if (noInvitesDeleted == 1 && noConfsDeleted == 1)
                return DELETE_OK;
            return DELETE_NOK;

        }

        @Override
        protected void onPostExecute(Integer rowsDeleted) {
            if (rowsDeleted == DELETE_OK) {
                //open the main activity
                Intent mainActivityIntent = new Intent(AdminConferenceDetailsActivity.this, AdminMainActivity.class);
                //clear the intent stack so that the user can't return to this activity
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivityIntent);
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminConferenceDetailsActivity.this);
                builder.setMessage(R.string.admin_delete_conf_error)
                        .setTitle(R.string.generic_error_occurred)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    /**********************************************************END OF ASYNC METHODS****************************************************************/
}
