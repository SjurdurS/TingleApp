package mmad.sjurdur.tingle.Outpan;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * Created by sjurdur on 26/04/16.
 * <p/>
 * This class is mainly based on this implementation.
 * https://github.com/johncipponeri/outpan-api-java/blob/master/src/io/github/johncipponeri/outpanapi/OutpanObject.java
 * <p/>
 * This class maps a json response from www.outpan.com to
 * an object for easy accessing of attributes.
 */
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

            // This will give us whatever's at "attributes", regardless of its type.
            Object attr = json.get("attributes");

            // `instanceof` tells us whether the object can be cast to a specific type
            if (attr instanceof JSONArray) {
                Log.i("OutpanObject", "It's a json array. Do nothing.");
            } else {
                Log.i("OutpanObject", "It's a json object.");
                // if you know it's either an array or an object, then it's an object
                JSONObject urlObject = (JSONObject) attr;
                // do objecty stuff with urlObject

                JSONObject attrObject = json.getJSONObject("attributes");

                List<String> keyList = new ArrayList<>();
                for (Iterator<String> it = attrObject.keys(); it.hasNext(); ) {
                    String key = it.next();
                    keyList.add(key);
                }

                String[] attrs = keyList.toArray(new String[keyList.size()]);

                for (int a = 0; a < attrs.length; a++)
                    this.attributes.put(attrs[a], attrObject.getString(attrs[a]));
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
