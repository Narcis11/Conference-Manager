package com.example.conferencemanager.admin;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.conferencemanager.R;
import com.example.conferencemanager.utilities.Constants;

public class AdminConferenceDetailsActivity extends AppCompatActivity{

    private static final String LOG_TAG = AdminConferenceDetailsActivity.class.getSimpleName();
    Bundle mBundle;
    //the titles
    private TextView mTitleTextView;
    private TextView mAddressTextView;
    private TextView mDescriptionTextView;
    private TextView mDateTextView;
    //the values
    private EditText mTitleValue;
    private EditText mAddressValue;
    private EditText mDescriptionValue;
    private EditText mDateValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity_conference_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mBundle = getIntent().getExtras();
        String actionBarTitle = mBundle.getString(Constants.BUNDLE_ADMIN_CONF_TITLE_KEY);
        getSupportActionBar().setTitle(actionBarTitle);
        loadUiElements();
        /*TextView mTitle = (TextView) findViewById(R.id.admin_conf_details_title);
        final EditText mTitleValue = (EditText) findViewById(R.id.admin_conf_details_title_value);
        mTitleValue.setText(actionBarTitle);
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(LOG_TAG, "in onclick");
                mTitleValue.setEnabled(true);
            }
        });*/
        //mTitleValue.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu_conf_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_conf) {
            Log.i(LOG_TAG,"Clicked on the edit button");
        }
        else if (id == R.id.action_delete_conf) {
            Log.i(LOG_TAG,"Clicked on the delete button");
        }
        return super.onOptionsItemSelected(item);
    }

    /**********************************************************START OF UI METHODS****************************************************************/
    private void loadUiElements() {
        mTitleTextView = (TextView) findViewById(R.id.admin_conf_details_title);
        mAddressTextView = (TextView) findViewById(R.id.admin_conf_details_address);
        mDescriptionTextView = (TextView) findViewById(R.id.admin_conf_details_description);
        mDateTextView = (TextView) findViewById(R.id.admin_conf_details_date);
        //the edittexts
        mTitleValue = (EditText) findViewById(R.id.admin_conf_details_title_value);
        mAddressValue = (EditText) findViewById(R.id.admin_conf_details_address_value);
        mDescriptionValue = (EditText) findViewById(R.id.admin_conf_details_description_value);
        mDateValue = (EditText) findViewById(R.id.admin_conf_details_date_value);
        //set the texts
        mTitleValue.setText(mBundle.getString(Constants.BUNDLE_ADMIN_CONF_TITLE_KEY));
        mAddressValue.setText(mBundle.getString(Constants.BUNDLE_ADMIN_CONF_ADDRESS_KEY));
        mDescriptionValue.setText(mBundle.getString(Constants.BUNDLE_ADMIN_CONF_DESCRIPTION_KEY));
        mDateValue.setText(mBundle.getString(Constants.BUNDLE_ADMIN_CONF_DATE_KEY));
    }

    /**********************************************************END OF UI METHODS****************************************************************/
}
