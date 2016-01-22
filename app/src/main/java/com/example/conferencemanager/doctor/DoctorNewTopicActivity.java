package com.example.conferencemanager.doctor;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.conferencemanager.R;
import com.example.conferencemanager.admin.AdminMainActivity;
import com.example.conferencemanager.data.UsersContract;
import com.example.conferencemanager.utilities.Constants;
import com.example.conferencemanager.utilities.SecurePreferences;

import java.util.Vector;

public class DoctorNewTopicActivity extends AppCompatActivity {

    private final String LOG_TAG = DoctorNewTopicActivity.class.getSimpleName();
    private Context mContext;
    //the edittext views
    private EditText mTitleValue;
    private EditText mDescriptionValue;
    //the error messages
    private TextView mTitleError;
    private TextView mDescriptionError;
    //the add topic button
    private Button mAddTopicButton;
    //generic error message
    private static String EMPTY_FIELD_ERROR = "";
    //used to get the doctor's username
    private SecurePreferences mSecurePreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity_new_topic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = getApplicationContext();
        mSecurePreferences = new SecurePreferences(mContext, Constants.PREF_CREDENTIALS, Constants.PREF_CREDENTIALS_KEY, true);
        EMPTY_FIELD_ERROR = getResources().getString(R.string.no_input);
        loadUIElements();
        setOnClickListeners();
    }

    /**************************************START OF UI/onClick METHODS*****************************************/
    private void loadUIElements() {
        //the edittext views
        mTitleValue = (EditText) findViewById(R.id.doctor_new_topic_title_value);
        mDescriptionValue = (EditText) findViewById(R.id.doctor_new_topic_description_value);
        //the error textviews
        mTitleError = (TextView) findViewById(R.id.doctor_new_topic_title_error);
        mDescriptionError = (TextView) findViewById(R.id.doctor_new_topic_description_error);
        //the add conference button
        mAddTopicButton = (Button) findViewById(R.id.doctor_new_topic_button);
    }

    private void setOnClickListeners() {
        mAddTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllFields();
            }
        });
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

    private void checkAllFields() {
        if (checkTitleField() && checkDescriptionField()) {
            Log.i(LOG_TAG, "Ready to save the conference");
            String[] topicValues = new String[5];
            topicValues[0] = mTitleValue.getText().toString();
            topicValues[1] = mDescriptionValue.getText().toString();
            new SaveNewTopicAsync().execute(topicValues);
        }
        else {
            if (!checkTitleField()) {
                //intentionally blank
            }
            if (!checkDescriptionField()) {
                //intentionally blank
            }
        }
    }
    /*****************************************END OF CHECK METHODS*************************************/


    /*****************************************START OF ASYNC METHODS*************************************/
    private class SaveNewTopicAsync extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Vector<ContentValues> cVVector = new Vector<>(1);
            ContentValues topicValues = new ContentValues();
            final String doctorUsername = mSecurePreferences.getString(Constants.PREF_DOCTOR_USERNAME_KEY);

            topicValues.put(UsersContract.TopicsEntry.COLUMN_TOPIC_TITLE, params[0]);
            topicValues.put(UsersContract.TopicsEntry.COLUMN_TOPIC_DESCRIPTION, params[1]);
            topicValues.put(UsersContract.TopicsEntry.COLUMN_TOPIC_ADDED_BY, params[2]);

            cVVector.add(topicValues);
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);

            return mContext.getContentResolver().bulkInsert(UsersContract.TopicsEntry.CONTENT_URI, cvArray);
        }

        @Override
        protected void onPostExecute(Integer rowsInserted) {
            if (rowsInserted == 1) {
                //open the main activity
                Intent mainActivityIntent = new Intent(DoctorNewTopicActivity.this, DoctorMainActivity.class);
                //clear the intent stack so that the user can't return to this activity
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivityIntent);
            }
            else {//an error occurred
                AlertDialog.Builder builder = new AlertDialog.Builder(DoctorNewTopicActivity.this);
                builder.setMessage(R.string.doctor_new_topic_error)
                        .setTitle(R.string.generic_error_occurred)
                        .setPositiveButton(android.R.string.ok, null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }
    /*****************************************END OF ASYNC METHODS*************************************/

}
