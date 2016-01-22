package com.example.conferencemanager.doctor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.conferencemanager.LandingPageActivity;
import com.example.conferencemanager.R;
import com.example.conferencemanager.admin.AdminConferencesFragment;
import com.example.conferencemanager.admin.AdminTopicsFragment;
import com.example.conferencemanager.utilities.SecurePreferences;
import com.example.conferencemanager.utilities.SlidingTabLayout;

import static com.example.conferencemanager.utilities.Constants.PREF_CREDENTIALS;
import static com.example.conferencemanager.utilities.Constants.PREF_CREDENTIALS_KEY;

public class DoctorMainActivity extends AppCompatActivity implements DoctorTopicsFragment.OnFragmentInteractionListener,
            DoctorInvitesFragment.OnFragmentInteractionListener{

    private SecurePreferences mSecurePreferences;
    private Context mContext;
    //used for creating the tabs
    SectionsPagerAdapter mSectionsPagerAdapter;
    private SlidingTabLayout mSlidingTabLayout;
    public static int CANVAS_BACKGROUND_COLOR; //used in SlidingTabStrip to set the background color of the tabs
    public ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity_main);
        //"hide" the elevation
        getSupportActionBar().setElevation(0);
        mContext = getApplicationContext();
        mSecurePreferences = new SecurePreferences(mContext, PREF_CREDENTIALS, PREF_CREDENTIALS_KEY, true);
        //tabs section
        //used for creating the sliding tabs section
        CANVAS_BACKGROUND_COLOR = ContextCompat.getColor(mContext, R.color.colorPrimary);//the background colour of the sliding tabs
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.doctor_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.doctor_sliding_tabs);

        mSlidingTabLayout.setDistributeEvenly(true);//the titles are evenly shown
        mSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;//set the tab's indicator colour
            }
        });
        mSlidingTabLayout.setViewPager(mViewPager);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /***********************************START OF METHODS USED BY THE SLIDING TABS******************************************/
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private final FragmentManager mFragmentManager;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }
        private int NO_OF_PAGES = 2;//the number of tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return DoctorTopicsFragment.newInstance();
                case 1: return DoctorInvitesFragment.newInstance();
                default: return DoctorTopicsFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return NO_OF_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_doctor_fragment_topics);
                case 1:
                    return getString(R.string.title_doctor_fragment_invites);
                default:
                    return getString(R.string.title_doctor_fragment_topics);
            }
        }
    }

    /***********************************END OF METHODS USED BY THE SLIDING TABS******************************************/

}
