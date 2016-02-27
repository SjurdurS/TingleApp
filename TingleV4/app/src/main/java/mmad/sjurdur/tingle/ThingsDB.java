package mmad.sjurdur.tingle;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sjurdur on 22/02/16.
 */
public class ThingsDB {

    private static ThingsDB sThingsDB;
    // fake database
    private List<Thing> mThingsDB;

    public static ThingsDB get() {
        if (sThingsDB == null) {
            sThingsDB = new ThingsDB();
        }
        return sThingsDB;
    }

    public List<Thing> getThingsDB() {
        return mThingsDB;
    }

    public void addThing(Thing thing) {
        mThingsDB.add(thing);
    }

    public int size() {
        return mThingsDB.size();
    }

    public Thing get(int i) {
        return mThingsDB.get(i);
    }

    /**
     * Remove a Thing from the database.
     * @param location  Integer position of the thing in the list.
     * @return returns the removed Thing.
     */
    public Thing remove(int location) {
        return sThingsDB.getThingsDB().remove(location);
    }

    // Fill database for testing purposes
    private ThingsDB() {
        mThingsDB = new ArrayList<Thing>();
        mThingsDB.add(new Thing("Android Phone", "Desk"));
        mThingsDB.add(new Thing("Android USB Cable", "Top Drawer"));
        mThingsDB.add(new Thing("Big Nerd book", "Table Top"));
        mThingsDB.add(new Thing("Raspberry Pi", "Black Box"));
        mThingsDB.add(new Thing("TV Tuner", "Next to the TV"));

        sort_database();
    }

    // Helper function to sort the fake database
    private void sort_database() {
        Collections.sort(mThingsDB, new Comparator<Thing>() {
            @Override
            public int compare(Thing lhs, Thing rhs) {
                return lhs.getWhat().compareToIgnoreCase(rhs.getWhat());
            }
        });
    }
}
