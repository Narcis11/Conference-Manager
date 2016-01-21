package com.example.conferencemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.conferencemanager.admin.AdminLoginActivity;
import com.example.conferencemanager.doctor.DoctorLoginActivity;
import com.example.conferencemanager.utilities.Constants;
import com.example.conferencemanager.utilities.SecurePreferences;

public class LandingPageActivity extends AppCompatActivity {

    private Button mDoctorButton;
    private Button mAdminButton;
    private SecurePreferences mSecurePreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_activity_landing_page);
        mSecurePreferences = new SecurePreferences(getApplicationContext(), Constants.PREF_CREDENTIALS, Constants.PREF_CREDENTIALS_KEY, true);
        //load the UI elements
        mDoctorButton = (Button) findViewById(R.id.landing_doctor_button);
        mAdminButton = (Button) findViewById(R.id.landing_admin_button);
        //set the onClick listeners
        setOnClickListeners();
        checkUserAlreadyLoggedIn();
    }

    /*******************************************START OF onClick METHODS******************************************/
    private void setOnClickListeners() {
        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminLoginActivity();
            }
        });

        mDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDoctorLoginActivity();
            }
        });

    }

    private void openAdminLoginActivity() {
        Intent adminIntent = new Intent(this, AdminLoginActivity.class);
        startActivity(adminIntent);
    }

    private void openDoctorLoginActivity() {
        Intent adminIntent = new Intent(this, DoctorLoginActivity.class);
        startActivity(adminIntent);
    }

    private void openAdminMainActivity() {

    }
    /*******************************************END OF onClick/NAVIGATION METHODS******************************************/

    private void checkUserAlreadyLoggedIn() {
        boolean mIsAdminLoggedIn = (mSecurePreferences.getString(Constants.PREF_IS_ADMIN_LOGGED_IN_KEY) != null &&
                mSecurePreferences.getString(Constants.PREF_IS_ADMIN_LOGGED_IN_KEY).equals(Constants.PREF_IS_ADMIN_LOGGED_IN_TRUE));
        if (mIsAdminLoggedIn)
            openAdminMainActivity();
    }


}
