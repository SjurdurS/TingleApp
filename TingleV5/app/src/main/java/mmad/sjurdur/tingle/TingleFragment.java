package mmad.sjurdur.tingle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import mmad.sjurdur.tingle.Outpan.FetchOutpanTask;
import mmad.sjurdur.tingle.Outpan.NetworkHelper;

public class TingleFragment extends Fragment {
    ListFragment.UpdateListFragmentListener mCallback;

    // Fake database
    private static ThingsDB mThingsDB;

    // GUI variables
    private Button mAddThingButton;
    private Button mSearchButton;
    private Button mAllThingsButton;
    private Button mScanBarcodeButton;
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

        // Barcode Scanner
        mScanBarcodeButton = (Button) v.findViewById(R.id.scan_barcode_button);

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


        mScanBarcodeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (NetworkHelper.isOnline(getContext())) {
                    String barcode = "9780134171456";
                    //String barcode = "0078915030900";
                    new FetchOutpanTask().execute(barcode);
                } else {
                    Log.i("ScanBarcodeButton", "No internet connection.");
                }

            }
        });
//        try {
//            final Intent scanIntent = new Intent("com.google.zxing.client.android.SCAN");
//            mScanBarcodeButton.setOnClickListener(new View.OnClickListener() {
//
//                public void onClick(View v) {
//                    scanIntent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                    startActivityForResult(scanIntent, 0);
//                }
//
//            });
//
//            PackageManager packageManager = getActivity().getPackageManager();
//            if (packageManager.resolveActivity(scanIntent,
//                    PackageManager.MATCH_DEFAULT_ONLY) == null) {
//                mScanBarcodeButton.setEnabled(false);
//            }
//
//        } catch (ActivityNotFoundException anfe) {
//            Log.e("onCreate", "Scanner Not Found", anfe);
//        }


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        /**
         * TODO: FIX THIS WHEN I CAN TEST ON ANNAs PHONE
         * TODO: UPDATE THE WHAT TEXT FIELD WITH THE NAME RETURNED FROM OUTPUT USING THE FOUND BARCODE
         * TODO: HANDLE IF NO ITEM IS FOUND WITH GIVEN BARCODE
         */
        if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                // Handle successful scan
                Toast toast = Toast.makeText(getActivity(), "Content:" + contents + " Format:" + format , Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            } else if (resultCode == Activity.RESULT_CANCELED) {

                // Handle cancel
                Toast toast = Toast.makeText(getActivity(), "Scan was Cancelled!", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.TOP, 25, 400);
                toast.show();
            }
        }
    }

    private void updateUI() {
        Thing lastAddedThing = mThingsDB.getLastAddedThing();
        if (lastAddedThing != null){
            mLastAdded.setText(lastAddedThing.toString());
        } else {
            // Clear the last added string when no Things are in the database.
            mLastAdded.setText("");
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