package com.example.conferencemanager.doctor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.conferencemanager.R;
import com.example.conferencemanager.data.UsersContract;

/**
 * Used to display the topics.
 */
public class DoctorTopicsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private OnFragmentInteractionListener mListener;
    private Context mContext;
    private View mRootView;
    private ListView mListView;
    private SimpleCursorAdapter mTopicsAdapter;
    private TextView mNoTopicsTextView;
    //Used for the database queries
    private static final String[] TOPICS_COLUMNS = {
            UsersContract.TopicsEntry.TABLE_NAME + "." + UsersContract.TopicsEntry._ID,
            UsersContract.TopicsEntry.COLUMN_TOPIC_TITLE,
            UsersContract.TopicsEntry.COLUMN_TOPIC_DESCRIPTION
    };
    //indices for each column
    private static final int COL_TOPIC_ID = 0;
    private static final int COL_TOPIC_TITLE = 1;
    private static final int COL_TOPIC_DESCRIPTION = 2;
    //loader identifier
    private static final int TOPICS_LOADER_ID = 1;

    public DoctorTopicsFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static DoctorTopicsFragment newInstance() {
        DoctorTopicsFragment fragment = new DoctorTopicsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        getLoaderManager().initLoader(TOPICS_LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mTopicsAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.doctor_fragment_topics_row,
                null,
                new String[] {
                        UsersContract.TopicsEntry.COLUMN_TOPIC_TITLE,
                        UsersContract.TopicsEntry.COLUMN_TOPIC_DESCRIPTION
                },
                new int[] {
                        R.id.doctor_fragment_topics_title_row,
                        R.id.doctor_fragment_topics_description_row
                },
                0
        );
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.doctor_fragment_topics, container, false);
        mListView = (ListView) mRootView.findViewById(R.id.doctor_topics_listview);
        mNoTopicsTextView = (TextView) mRootView.findViewById(R.id.doctor_topics_no_rows);
        mListView.setAdapter(mTopicsAdapter);
        mTopicsAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                switch (columnIndex) {
                    case COL_TOPIC_ID: ((TextView) view).setText(cursor.getString(COL_TOPIC_ID)); return true;
                    case COL_TOPIC_TITLE: ((TextView) view).setText(cursor.getString(COL_TOPIC_TITLE)); return true;
                    case COL_TOPIC_DESCRIPTION: ((TextView) view).setText(cursor.getString(COL_TOPIC_DESCRIPTION)); return true;
                }
                return true;
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
                UsersContract.TopicsEntry.CONTENT_URI,
                TOPICS_COLUMNS,
                null,
                null,
                UsersContract.TopicsEntry.SORT_ORDER
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTopicsAdapter.swapCursor(data);
        //display a message if no topics are available
        if (data.getCount() > 0) {
            if (mListView.getVisibility() != View.VISIBLE)
                mListView.setVisibility(View.VISIBLE);
            if (mNoTopicsTextView.getVisibility() == View.VISIBLE)
                mNoTopicsTextView.setVisibility(View.GONE);
        }
        else {
            mListView.setVisibility(View.GONE);
            mNoTopicsTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTopicsAdapter.swapCursor(null);
    }

    /************************************************END OF DATABASE METHODS**********************************************/
}
