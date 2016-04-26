package mmad.sjurdur.tingle.Outpan;

/**
 * Created by sjurdur on 26/04/16.
 *
 * This class is mainly based on this implementation.
 * https://github.com/johncipponeri/outpan-api-java/blob/master/src/io/github/johncipponeri/outpanapi/OutpanObject.java
 */
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;



public class OutpanObject {

    public String
            gtin,
            outpan_url,
            name;

    public HashMap<String, String>
            attributes;

    public ArrayList<String>
            images,
            videos;

    public OutpanObject() {
        this.gtin = "";
        this.outpan_url = "";
        this.name = "";

        this.attributes = new HashMap<String, String>();
        this.images = new ArrayList<String>();
        this.videos = new ArrayList<String>();
    }

    public OutpanObject(JSONObject json) throws JSONException {
        // Call main constructor
        this();

        this.gtin = json.getString("gtin");
        this.outpan_url = json.getString("outpan_url");

        if (!json.isNull("name"))
            this.name = json.getString("name");

        if (!json.isNull("attributes")) {

            Log.i("OutpanObject",json.get("attributes").toString());
            try {
                JSONObject attrObject = json.getJSONObject("attributes");

                List<String> keyList = new ArrayList<>();
                for (Iterator<String> it = attrObject.keys(); it.hasNext(); ) {
                    String key = it.next();
                    keyList.add(key);
                }

                String[] attrs = keyList.toArray(new String[keyList.size()]);

                for (int a = 0; a < attrs.length; a++)
                    this.attributes.put(attrs[a], attrObject.getString(attrs[a]));
            } catch (Exception e){
                Log.e("OutpanObject", "Error: " + e);
            }

        }

        if (!json.isNull("images")) {
            JSONArray imgs = json.getJSONArray("images");
            for (int i = 0; i < imgs.length(); i++)
                this.images.add(imgs.getString(i));
        }

        if (!json.isNull("videos")) {
            JSONArray vids = json.getJSONArray("videos");
            for (int i = 0; i < vids.length(); i++)
                this.videos.add(vids.getString(i));
        }
    }
}
