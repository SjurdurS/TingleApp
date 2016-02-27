package mmad.sjurdur.tingle;

/**
 * Created by sjurdur on 22/02/16.
 */

import android.content.Context;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

// here's our beautiful adapter
public class ArrayAdapterItem extends ArrayAdapter<Thing> {

    Context mContext;
    int layoutResourceId;

    private ThingsDB mThingsDB;

    public ArrayAdapterItem(Context mContext, int layoutResourceId, ThingsDB thingsDB) {

        super(mContext, layoutResourceId, thingsDB.getThingsDB());

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mThingsDB = thingsDB;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /*
         * The convertView argument is essentially a "ScrapView" as described is Lucas post
         * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
         * It will have a non-null value when ListView is asking you recycle the row layout.
         * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
         */
        if(convertView==null){
            // inflate the layout
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
        }

        // object item based on the position
        Thing thing = mThingsDB.get(position);

        // get the TextView and then set the text (item name) and tag (item ID) values
        ToggledTextView textViewItem = (ToggledTextView) convertView.findViewById(R.id.textViewItem);
        textViewItem.setText(thing.getWhat());
        textViewItem.setTag(false);

        return convertView;

    }

}