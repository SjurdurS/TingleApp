package mmad.sjurdur.tingle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TingleFragment extends Fragment {
    ListFragment.UpdateListFragmentListener mCallback;

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
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        if (context instanceof ListActivity || context instanceof TingleActivity) {
            try {
                mCallback = (ListFragment.UpdateListFragmentListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement UpdateListFragmentListener");
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThingsDB = ThingsDB.get(getActivity());
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

                    mCallback.onUpdateListFragment();

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

    private void updateUI() {
        Thing lastAddedThing = mThingsDB.getLastAddedThing();
        if (lastAddedThing != null){
            mLastAdded.setText(lastAddedThing.toString());
        }
    }


    private void display_toast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void display_toast(int message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    private void search_for_thing(String what) {

        Thing foundThing = mThingsDB.search(what);
        if (foundThing != null) {
            display_toast(foundThing.getWhat() + " is here:\n" + foundThing.getWhere());
        } else {
            display_toast(R.string.search_not_found);
        }
    }
}