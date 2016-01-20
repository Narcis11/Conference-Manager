package com.example.conferencemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.conferencemanager.admin.AdminLoginActivity;

public class LandingPageActivity extends AppCompatActivity {

    private Button mDoctorButton;
    private Button mAdminButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_activity_landing_page);

        //load the UI elements
        mDoctorButton = (Button) findViewById(R.id.landing_doctor_button);
        mAdminButton = (Button) findViewById(R.id.landing_admin_button);
        //set the onClick listeners
        setOnClickListeners();
    }

    /*******************************************START OF onClick METHODS******************************************/
    private void setOnClickListeners() {
        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAdminLoginActivity();
            }
        });

    }

    private void openAdminLoginActivity() {
        Intent adminIntent = new Intent(this, AdminLoginActivity.class);
        startActivity(adminIntent);
    }

    /*******************************************END OF onClick/NAVIGATION METHODS******************************************/


}
