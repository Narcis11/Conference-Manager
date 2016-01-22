package com.example.conferencemanager.admin;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.conferencemanager.R;
import com.example.conferencemanager.data.UsersContract;
import com.example.conferencemanager.utilities.Constants;

/**
 This fragment is used to display the conferences for an admin.
 */
public class AdminConferencesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private OnFragmentInteractionListener mListener;
    private Context mContext;
    private View mRootView;
    private ListView mListView;
    private SimpleCursorAdapter mConferencesAdapter;
    private TextView mNoConferencesTextView;
    //Used for the database queries
    private static final String[] CONFERENCES_COLUMNS = {
            UsersContract.ConferencesEntry.TABLE_NAME + "." + UsersContract.ConferencesEntry._ID,
            UsersContract.ConferencesEntry.COLUMN_CONF_TITLE,
            UsersContract.ConferencesEntry.COLUMN_CONF_ADDRESS,
            UsersContract.ConferencesEntry.COLUMN_CONF_DATE,
            UsersContract.ConferencesEntry.COLUMN_CONF_DESCRIPTION
    };
    //indices for each column
    private static final int COL_CONF_ID = 0;
    private static final int COL_CONF_TITLE = 1;
    private static final int COL_CONF_ADDRESS = 2;
    private static final int COL_CONF_DATE = 3;
    private static final int COL_CONF_DESCRIPTION = 4;
    //loader identifier
    private static final int CONFERENCES_LOADER_ID = 1;

    public AdminConferencesFragment() {
        // Required empty public constructor
    }

    public static AdminConferencesFragment newInstance() {
        AdminConferencesFragment fragment = new AdminConferencesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        getLoaderManager().initLoader(CONFERENCES_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mConferencesAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.admin_fragment_conferences_row,
                null,
                new String[] {
                        UsersContract.ConferencesEntry.COLUMN_CONF_TITLE,
                        UsersContract.ConferencesEntry.COLUMN_CONF_ADDRESS,
                        UsersContract.ConferencesEntry.COLUMN_CONF_DATE
                },
                new int[] {
                        R.id.admin_fragment_conf_title_row,
                        R.id.admin_fragment_conf_address_row,
                        R.id.admin_fragment_conf_date_row,
                },
                0
        );
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.admin_fragment_conferences, container, false);
        mListView = (ListView) mRootView.findViewById(R.id.admin_conferences_listview);
        FloatingActionButton addConferenceButton = (FloatingActionButton) mRootView.findViewById(R.id.admin_conferences_fab);
        mNoConferencesTextView = (TextView) mRootView.findViewById(R.id.admin_conferences_no_rows);
        mListView.setAdapter(mConferencesAdapter);
        mConferencesAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (columnIndex) {
                    case COL_CONF_TITLE:
                        ((TextView) view).setText(cursor.getString(COL_CONF_TITLE));
                        return true;
                    case COL_CONF_DATE:
                        ((TextView) view).setText(cursor.getString(COL_CONF_DATE));
                        return true;
                    case COL_CONF_ADDRESS:
                        ((TextView) view).setText(cursor.getString(COL_CONF_ADDRESS));
                        return true;
                }
                return true;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mConferencesAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    Intent detailsIntent = new Intent(getActivity(), AdminConferenceDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.BUNDLE_ADMIN_CONF_ID_KEY, cursor.getInt(COL_CONF_ID));
                    bundle.putString(Constants.BUNDLE_ADMIN_CONF_TITLE_KEY, cursor.getString(COL_CONF_TITLE));
                    bundle.putString(Constants.BUNDLE_ADMIN_CONF_ADDRESS_KEY, cursor.getString(COL_CONF_ADDRESS));
                    bundle.putString(Constants.BUNDLE_ADMIN_CONF_DESCRIPTION_KEY, cursor.getString(COL_CONF_DESCRIPTION));
                    bundle.putString(Constants.BUNDLE_ADMIN_CONF_DATE_KEY, cursor.getString(COL_CONF_DATE));
                    detailsIntent.putExtras(bundle);
                    startActivity(detailsIntent);

                }
            }
        });
        addConferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newConfIntent = new Intent(getActivity(), AdminNewConferenceActivity.class);
                startActivity(newConfIntent);
            }
        });
        // Inflate the layout for this fragment
        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /************************************************START OF DATABASE METHODS**********************************************/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new android.support.v4.content.CursorLoader(
                mContext,
                UsersContract.ConferencesEntry.CONTENT_URI,
                CONFERENCES_COLUMNS,
                null,
                null,
                UsersContract.ConferencesEntry.SORT_ORDER
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mConferencesAdapter.swapCursor(data);
        //display a message if no conferences are available
        if (data.getCount() > 0) {
            if (mListView.getVisibility() != View.VISIBLE)
                mListView.setVisibility(View.VISIBLE);
            if (mNoConferencesTextView.getVisibility() == View.VISIBLE)
                mNoConferencesTextView.setVisibility(View.GONE);
        }
        else {
            mListView.setVisibility(View.GONE);
            mNoConferencesTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mConferencesAdapter.swapCursor(null);
    }
    /************************************************END OF DATABASE METHODS**********************************************/
}
