package com.example.conferencemanager.doctor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.conferencemanager.LandingPageActivity;
import com.example.conferencemanager.R;
import com.example.conferencemanager.utilities.SecurePreferences;

import static com.example.conferencemanager.utilities.Constants.PREF_CREDENTIALS;
import static com.example.conferencemanager.utilities.Constants.PREF_CREDENTIALS_KEY;

public class DoctorMainActivity extends AppCompatActivity {

    private SecurePreferences mSecurePreferences;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity_main);
        mContext = getApplicationContext();
        mSecurePreferences = new SecurePreferences(mContext, PREF_CREDENTIALS, PREF_CREDENTIALS_KEY, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doctor_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            //clear the shared preferences
            mSecurePreferences.clear();
            //navigate to login
            Intent intent = new Intent(this, LandingPageActivity.class);
            //don't allow the user to return to this activity
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //start the activity
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
