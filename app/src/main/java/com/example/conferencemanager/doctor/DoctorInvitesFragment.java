package com.example.conferencemanager.doctor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
 * Used to display the doctor's invites.
 */
public class DoctorInvitesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    private OnFragmentInteractionListener mListener;
    private Context mContext;
    private View mRootView;
    private ListView mListView;
    private SimpleCursorAdapter mInvitesAdapter;
    private TextView mNoInvitesTextView;
    //Used for the database queries
    private static final String[] INVITES_COLUMNS = {
            UsersContract.ConferencesEntry.TABLE_NAME + "." + UsersContract.ConferencesEntry._ID,
            UsersContract.ConferencesEntry.COLUMN_CONF_TITLE,
            UsersContract.ConferencesEntry.COLUMN_CONF_ADDRESS,
            UsersContract.ConferencesEntry.COLUMN_CONF_DATE,
            UsersContract.ConferencesEntry.COLUMN_CONF_DESCRIPTION
    };
    //indices for each column
    private static final int COL_INVITE_ID = 0;
    private static final int COL_INVITE_TITLE = 1;
    private static final int COL_INVITE_ADDRESS = 2;
    private static final int COL_INVITE_DATE = 3;
    private static final int COL_INVITE_DESCRIPTION = 4;
    //loader identifier
    private static final int INVITES_LOADER_ID = 1;
    
    public DoctorInvitesFragment() {
        // Required empty public constructor
    }

    public static DoctorInvitesFragment newInstance() {
        DoctorInvitesFragment fragment = new DoctorInvitesFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        getLoaderManager().initLoader(INVITES_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInvitesAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.doctor_fragment_invites_row,
                null,
                new String[] {
                        UsersContract.ConferencesEntry.COLUMN_CONF_TITLE,
                        UsersContract.ConferencesEntry.COLUMN_CONF_ADDRESS,
                        UsersContract.ConferencesEntry.COLUMN_CONF_DATE
                },
                new int[] {
                        R.id.doctor_fragment_invite_title_row,
                        R.id.doctor_fragment_invite_address_row,
                        R.id.doctor_fragment_invite_date_row,
                },
                0
        );
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.doctor_fragment_invites, container, false);
        mListView = (ListView) mRootView.findViewById(R.id.doctor_invites_listview);
        mNoInvitesTextView = (TextView) mRootView.findViewById(R.id.doctor_invites_no_rows);
        mListView.setAdapter(mInvitesAdapter);
        mInvitesAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (columnIndex) {
                    case COL_INVITE_TITLE:
                        ((TextView) view).setText(cursor.getString(COL_INVITE_TITLE));
                        return true;
                    case COL_INVITE_DATE:
                        ((TextView) view).setText(cursor.getString(COL_INVITE_DATE));
                        return true;
                    case COL_INVITE_ADDRESS:
                        ((TextView) view).setText(cursor.getString(COL_INVITE_ADDRESS));
                        return true;
                }
                return true;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mInvitesAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    Intent detailsIntent = new Intent(getActivity(), DoctorInviteDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.BUNDLE_DOCTOR_INVITE_ID_KEY, cursor.getInt(COL_INVITE_ID));
                    bundle.putString(Constants.BUNDLE_DOCTOR_INVITE_TITLE_KEY, cursor.getString(COL_INVITE_TITLE));
                    bundle.putString(Constants.BUNDLE_DOCTOR_INVITE_ADDRESS_KEY, cursor.getString(COL_INVITE_ADDRESS));
                    bundle.putString(Constants.BUNDLE_DOCTOR_INVITE_DESCRIPTION_KEY, cursor.getString(COL_INVITE_DESCRIPTION));
                    bundle.putString(Constants.BUNDLE_DOCTOR_INVITE_DATE_KEY, cursor.getString(COL_INVITE_DATE));
                    detailsIntent.putExtras(bundle);
                    startActivity(detailsIntent);

                }
            }
        });
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
                INVITES_COLUMNS,
                null,
                null,
                UsersContract.ConferencesEntry.SORT_ORDER
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mInvitesAdapter.swapCursor(data);
        //display a message if no conferences are available
        if (data.getCount() > 0) {
            if (mListView.getVisibility() != View.VISIBLE)
                mListView.setVisibility(View.VISIBLE);
            if (mNoInvitesTextView.getVisibility() == View.VISIBLE)
                mNoInvitesTextView.setVisibility(View.GONE);
        }
        else {
            mListView.setVisibility(View.GONE);
            mNoInvitesTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mInvitesAdapter.swapCursor(null);
    }
    /************************************************END OF DATABASE METHODS**********************************************/
}
