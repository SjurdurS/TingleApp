package mmad.sjurdur.tingle;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sjurdur on 22/02/16.
 *
 * Fake database to store things.
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

    /**
     * Get the database.
     * @return Returns the singleton thingDatabase.
     */
    public List<Thing> getThingsDB() {
        return mThingsDB;
    }

    /**
     * Get the size of the database
     * @return Returns the size of the database.
     */
    public int size() {
        return mThingsDB.size();
    }

    public Thing get(int i) {
        return mThingsDB.get(i);
    }

    /**
     * Remove a Thing from the database.
     * @param location  Integer position of the thing in the list.
     * @return Returns the removed Thing.
     */
    public Thing remove(int location) {
        return sThingsDB.getThingsDB().remove(location);
    }

    /**
     * Add an item to the database.
     * @param thing Thing to add
     */
    public void addThing(Thing thing) {
        mThingsDB.add(thing);
        //sort_database();
    }

    /**
     * Fill database for testing purposes
     */
    private ThingsDB() {
        mThingsDB = new ArrayList<Thing>();
        mThingsDB.add(new Thing("Android Phone", "Desk"));
        mThingsDB.add(new Thing("Android USB Cable", "Top Drawer"));
        mThingsDB.add(new Thing("Big Nerd book", "Table Top"));
        mThingsDB.add(new Thing("Raspberry Pi", "Black Box"));
        mThingsDB.add(new Thing("TV Tuner", "Next to the TV"));

        //sort_database();
    }

    /**
     * Search the database for an thing.
     * @param what The name of the thing
     * @return Returns the first match of Thing found in the database. If no matches
     * are found null is returned.
     */
    public Thing search(String what) {
        for (Thing t : mThingsDB) {
            if (t.getWhat() != null && t.getWhat().toLowerCase().contains(what.toLowerCase())) {
                return t;
            }
        }

        return null;
    }

    /**
     *   !! Removed because Last Thing Added will !!
     *   !! not work when sorting the database    !!
     *
     *   Helper function to sort the fake databasw
     */
//    private void sort_database() {
//        Collections.sort(mThingsDB, new Comparator<Thing>() {
//            @Override
//            public int compare(Thing lhs, Thing rhs) {
//                return lhs.getWhat().compareToIgnoreCase(rhs.getWhat());
//            }
//        });
//    }
}
