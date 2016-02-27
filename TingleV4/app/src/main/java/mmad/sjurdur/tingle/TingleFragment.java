package mmad.sjurdur.tingle;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TingleFragment extends Fragment {

    // Fake database
    private static ThingsDB mThingsDB;

    // GUI variables
    private Button mAddThingButton;
    private Button mSearchButton;
    private Button mAllThingsButton;
    private TextView mLastAdded;
    private TextView mNewWhat, mNewWhere;
    private TextView mSearchWhat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThingsDB = ThingsDB.get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tingle, container, false);


        // Accessing GUI element
        mLastAdded = (TextView) v.findViewById(R.id.last_thing);
        updateUI();

        // Add Button
        mAddThingButton = (Button) v.findViewById(R.id.add_button);

        // Text fields for describing a thing
        mNewWhat = (TextView) v.findViewById(R.id.what_text);
        mNewWhere = (TextView) v.findViewById(R.id.where_text);

        // Search functions
        mSearchButton = (Button) v.findViewById(R.id.search_button);
        mSearchWhat = (TextView) v.findViewById(R.id.search_text_where);

        // view products click event
        mAddThingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((mNewWhat.getText().length() > 0) && (mNewWhere.getText().length() > 0)) {

                    mThingsDB.addThing(
                            new Thing(
                                    mNewWhat.getText().toString(),
                                    mNewWhere.getText().toString()
                            )
                    );

                    resetListFragment();

                    // Clear text fields
                    mNewWhat.setText("");
                    mNewWhere.setText("");
                    updateUI();
                }
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSearchWhat.getText().length() > 0) {
                    search_for_thing(mSearchWhat.getText().toString());
                }
            }
        });

        mAllThingsButton = (Button) v.findViewById(R.id.activity_list_button);
        mAllThingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start List Activity
                Intent listActivityIntent = new Intent(getActivity(), ListActivity.class);
                startActivity(listActivityIntent);
            }
        });

        return v;
    }

    private void resetListFragment() {
        //Call the Fragmentmanager
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FragmentManager manager = getActivity().getSupportFragmentManager();

            //collect fragment (layout)

            Fragment fragment_list = manager.findFragmentById(R.id.activity_list_fragment_container);

            //Use the manager to begin transaction, and remove the fragment
            manager.beginTransaction()
                    .remove(fragment_list)
                    .commit();

            //Create a new Fragment (myListFragment.java)
            fragment_list = new ListFragment();

            //Use the manager to begin transaction, and add the new(updated) fragment
            manager.beginTransaction()
                    .add(R.id.activity_list_fragment_container, fragment_list)
                    .commit();
        }
    }

    private void updateUI() {
        int s = mThingsDB.size();
        if (s > 0) {
            mLastAdded.setText(mThingsDB.get(s - 1).toString());
        }
    }


    private void display_toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void display_toast(int message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void search_for_thing(String what) {

        for (Thing t : mThingsDB.getThingsDB()) {
            if (t.getWhat() != null && t.getWhat().contains(what)) {
                display_toast(t.getWhere());
                return;
            }
        }

        display_toast(R.string.search_not_found);
    }
}