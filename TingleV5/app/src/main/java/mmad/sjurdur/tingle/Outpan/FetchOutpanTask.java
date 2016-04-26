package mmad.sjurdur.tingle.Outpan;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;

import mmad.sjurdur.tingle.Outpan.OutpanFetcher;

/**
 * Created by sjurdur on 26/04/16.
 * Based on the Outpan API Documentation
 * https://www.outpan.com/developers.php
 */
public class FetchOutpanTask extends AsyncTask<String, Void, OutpanObject> {
    private static final String TAG     = "FetchOutpanTask";
    private static final String API_KEY = "0d08313ee758182a42785762e24cf8ff";
    private String API_URL              = "https://api.outpan.com/v2/products/%s?apikey=%s";

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
            me.printStackTrace();
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch URL: " + ioe);
            ioe.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(OutpanObject outpan) {
        if (outpan != null) {
            Log.i(TAG, outpan.gtin);
            Log.i(TAG, outpan.name);
        } else {
            Log.e(TAG, "JSON is null.");
        }
    }
}
