package com.example.conferencemanager.admin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.conferencemanager.LandingPageActivity;
import com.example.conferencemanager.R;
import com.example.conferencemanager.utilities.Constants;
import com.example.conferencemanager.utilities.SecurePreferences;
import com.example.conferencemanager.utilities.SlidingTabLayout;

import static com.example.conferencemanager.utilities.Constants.*;

public class AdminMainActivity extends AppCompatActivity implements AdminConferencesFragment.OnFragmentInteractionListener,
                AdminTopicsFragment.OnFragmentInteractionListener{

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
        setContentView(R.layout.admin_activity_main);
        //"hide" the elevation
        getSupportActionBar().setElevation(0);
        mContext = getApplicationContext();
        mSecurePreferences = new SecurePreferences(mContext, PREF_CREDENTIALS, PREF_CREDENTIALS_KEY, true);
        //tabs section
        //used for creating the sliding tabs section
        CANVAS_BACKGROUND_COLOR = ContextCompat.getColor(mContext,R.color.colorPrimary);//the background colour of the sliding tabs
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

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
        getMenuInflater().inflate(R.menu.admin_menu_main, menu);
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


    /***********************************START OF METHODS USED BY THE SLIDING TABS******************************************/
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     * We need to extend FragmentStatePagerAdapter, not FragmentPagerAdapter, so that we are able to swap between List/Map in the
     * Shops fragment
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
        private final FragmentManager mFragmentManager;
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragmentManager = fm;
        }
        private int NO_OF_PAGES = 2;//the number of tabs
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            //   Log.i(LOG_TAG, "In getItem, WHICH_SHOPS_FRAGMENT: " + Constants.WHICH_SHOPS_FRAGMENT);
            //if you need to send parameters to the fragments, do it through newInstance
            switch (position) {
                case 0: return AdminConferencesFragment.newInstance();
                case 1: return AdminTopicsFragment.newInstance();
                default: return AdminTopicsFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return NO_OF_PAGES;
        }

        //used to update the UI
        @Override
        public int getItemPosition(Object object) {
            //this implementation recreates all the Views, which is not very efficient
            // Log.i(LOG_TAG," In getItemPosition");


   /*         if (object instanceof UpdateableSetupFragment)
                ((UpdateableSetupFragment) object).onSetupFragmentUpdate();
            if (object instanceof  UpdateableMapFragment)
                ((UpdateableMapFragment) object).onMapFragmentUpdate();*/
            return super.getItemPosition(object);

        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_fragment_conferences);
                case 1:
                    return getString(R.string.title_fragment_topics);
                default:
                    return getString(R.string.title_fragment_conferences);
            }
        }
    }

    //the method used for communicating with the fragments


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /***********************************END OF METHODS USED BY THE SLIDING TABS******************************************/
}
