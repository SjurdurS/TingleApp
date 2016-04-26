package mmad.sjurdur.tingle;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import mmad.sjurdur.tingle.Outpan.NetworkHelper;
import mmad.sjurdur.tingle.Outpan.OutpanFetcher;
import mmad.sjurdur.tingle.Outpan.OutpanObject;

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
    private TextView mBarcodeText;

    private Boolean mUseBarcodeScanner = false;

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
        mBarcodeText = (TextView) v.findViewById(R.id.barcodeText);


        // Add OnEditorActionListeners to handle when the user is done typing.
        mNewWhere.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // Instead of reimplemented the add thing function just click the button.
                    mAddThingButton.performClick();
                    HideKeyboard(mNewWhere);
                    handled = true;
                }
                return handled;
            }
        });

        mBarcodeText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {

                    LookupBarcode();
                    HideKeyboard(mBarcodeText);
                    handled = true;
                }
                return handled;
            }
        });

        mSearchWhat.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // Instead of reimplemented the add thing function just click the button.
                    mSearchButton.performClick();
                    HideKeyboard(mSearchButton);
                    handled = true;
                }
                return handled;
            }
        });


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

        try {
            final Intent scanIntent = new Intent("com.google.zxing.client.android.SCAN");
            mScanBarcodeButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    scanIntent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                    if (mUseBarcodeScanner) {
                        // Use the scanner
                        startActivityForResult(scanIntent, 0);
                    } else {
                        LookupBarcode();
                    }
                }

            });

            PackageManager packageManager = getActivity().getPackageManager();
            // If no barcode scanner is found on the app change the button to manual lookup.
            if (packageManager.resolveActivity(scanIntent,
                    PackageManager.MATCH_DEFAULT_ONLY) == null) {
                mScanBarcodeButton.setText(R.string.lookup_barcode_button_text);
                mUseBarcodeScanner = false;
            } else {
                mScanBarcodeButton.setText(R.string.scan_barcode_button_text);
                mUseBarcodeScanner = true;
            }

        } catch (ActivityNotFoundException anfe) {
            Log.e("onCreate", "Scanner Not Found", anfe);
        }


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

    private void LookupBarcode(){
        String barcode = mBarcodeText.getText().toString();

        if (NetworkHelper.isOnline(getContext())) {
            new FetchOutpanTask().execute(barcode);
        } else {
            display_toast("No internet connection.");
        }
    }

    private void HideKeyboard(TextView textView) {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(textView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
                String barcode = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                // Handle successful scan
                if (NetworkHelper.isOnline(getContext())) {
                    mBarcodeText.setText(barcode);
                    new FetchOutpanTask().execute(barcode);
                } else {
                    Log.i("ScanBarcodeButton", "No internet connection.");
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {

                // Handle cancel
                display_toast("Scan was Cancelled!");
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


    /**
     * Created by sjurdur on 26/04/16.
     * Based on the Outpan API Documentation
     * https://www.outpan.com/developers.php
     */
    private class FetchOutpanTask extends AsyncTask<String, Void, OutpanObject> {
        private static final String TAG     = "FetchOutpanTask";
        private static final String API_KEY = "0d08313ee758182a42785762e24cf8ff";
        private String API_URL              = "https://api.outpan.com/v2/products/%s?apikey=%s";
        private String errorMessage = "";

        @Override
        protected OutpanObject doInBackground(String... params) {

            try {
                if (params.length != 1) {
                    Log.e(TAG, "Not enough arguments.");
                } else {
                    String gtin = params[0];
                    String url = String.format(API_URL, gtin, API_KEY);
                    Log.i(TAG, url);
                    JSONObject json = new OutpanFetcher().getProductInfo(url);
                    Log.i(TAG, "Fetched contents of URL: " + json);
                    OutpanObject outpanObject = new OutpanObject(json);
                    return outpanObject;
                }
            } catch (MalformedURLException me) {
                errorMessage = "MalformedURL: " + me.getMessage();
                me.printStackTrace();
            } catch (FileNotFoundException fnfe) {
                errorMessage = "Error: No item found:\n" + fnfe.getMessage();
                fnfe.printStackTrace();
            } catch (IOException ioe) {
                errorMessage = "Error: Could not fetch url:\n" + ioe.getMessage();
                ioe.printStackTrace();
            } catch (JSONException e) {
                errorMessage = "JSONException: " + e.getMessage();
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(OutpanObject outpan) {
            if (!errorMessage.equals("")) {
                display_toast(errorMessage);
            }
            if (outpan != null) {
                if (outpan.name != null && !outpan.name.equals("")) {
                    mNewWhat.setText(outpan.name);
                    // Let the user type in Where now.
                    mNewWhere.requestFocus();
                } else {
                    // Handle if no name was found.
                    display_toast("No name found for barcode: " +outpan.gtin);
                }
            } else {
                Log.e(TAG, "JSON is null.");
            }
        }
    }
}