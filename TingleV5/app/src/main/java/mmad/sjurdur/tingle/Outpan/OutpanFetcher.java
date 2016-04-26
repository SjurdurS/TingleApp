package mmad.sjurdur.tingle.Outpan;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sjurdur on 26/04/16.
 */
public class OutpanFetcher {


    public JSONObject getProductInfo(String urlSpec) throws IOException, JSONException {

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(10000 /* milliseconds */);
        connection.setConnectTimeout(15000 /* milliseconds */);
        //connection.setRequestMethod("GET");
        //connection.setDoInput(true);
        connection.connect();

        InputStream stream = null;

        try {
            stream = connection.getInputStream();
            Log.i("OutpanFecther", "ResponseCode: "+ connection.getResponseCode());
            if(connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST) {
                throw new IOException(connection.getResponseMessage() + ": badrequest: " + urlSpec);
            }
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            String jsonString = new String();

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            jsonString = sb.toString();

            return new JSONObject(jsonString);

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            // Makes sure that the InputStream is closed after the app is
            // finished using it.
            if (stream != null) {
                stream.close();
            }
        }
    }
}
