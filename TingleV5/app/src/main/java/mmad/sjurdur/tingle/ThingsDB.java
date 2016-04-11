package mmad.sjurdur.tingle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mmad.sjurdur.tingle.database.ThingsCursorWrapper;
import mmad.sjurdur.tingle.database.ThingsDbSchema;
import mmad.sjurdur.tingle.database.ThingsDbSchema.ThingsTable;

/**
 * Created by sjurdur on 22/02/16.
 *
 * Fake database to store things.
 */
public class ThingsDB {

    private static ThingsDB sThingsDB;

    private SQLiteDatabase mDatabase;

    public static ThingsDB get() {
        if (sThingsDB == null) {
            sThingsDB = new ThingsDB();
        }
        return sThingsDB;
    }

    /**
     * Get the contents of the databse.
     * @return Returns all things in the database.
     */
    public List<Thing> getThings() {

        List<Thing> things = new ArrayList<>();

        ThingsCursorWrapper cursor = queryThings(null, null);

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                Thing thing = cursor.getThing();
                things.add(thing);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return things;
    }

    /**
     * Get the number of things in the database
     * @return Returns the number of things in the database.
     */
    public int size() {
        return getThings().size();
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


    private static ContentValues getContentValues(Thing thing) {
        ContentValues values = new ContentValues();
        values.put(ThingsTable.Cols.UUID, thing.getId().toString());
        values.put(ThingsTable.Cols.WHAT, thing.getWhat());
        values.put(ThingsTable.Cols.WHERE, thing.getWhere());

        return values;
    }

    private ThingsCursorWrapper queryThings(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ThingsTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new ThingsCursorWrapper(cursor);
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
