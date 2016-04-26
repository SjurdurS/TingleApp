package mmad.sjurdur.tingle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import mmad.sjurdur.tingle.database.ThingsBaseHelper;
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

    private Context mContext;

    private SQLiteDatabase mDatabase;

    /**
     * Private constructor as part of the singleton pattern.
     */
    private ThingsDB(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ThingsBaseHelper(mContext).getWritableDatabase();
    }

    public static ThingsDB get(Context context) {
        if (sThingsDB == null) {
            sThingsDB = new ThingsDB(context);
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

    public Thing getThing(UUID id) {
        ThingsCursorWrapper cursor = queryThings(
                ThingsTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getThing();
        } finally {
            cursor.close();
        }
    }

    /**
     * Remove a Thing from the database.
     * @param thing The thing to remove from the database.
     * @return Returns true if the thing was successfully removed.
     */
    public boolean remove(Thing thing) {
        String uuidString = thing.getId().toString();

        return mDatabase.delete(ThingsTable.NAME,
                ThingsTable.Cols.UUID + " = ?",
                new String[]{uuidString}) > 0;
    }

    /**
     * Add an item to the database.
     * @param thing Thing to add
     */
    public void addThing(Thing thing) {
        ContentValues values = getContentValues(thing);
        mDatabase.insert(ThingsTable.NAME, null, values);
    }

    /**
     * Update a thing in the databse.
     * @param thing The thing to update.
     */
    public void updateThing(Thing thing) {
        String uuidString = thing.getId().toString();
        ContentValues values = getContentValues(thing);

        mDatabase.update(ThingsTable.NAME, values,
                ThingsTable.Cols.UUID + " = ?",
                new String[]{uuidString});
    }


    public Thing getLastAddedThing(){
        Cursor cursor = mDatabase.query(ThingsTable.NAME, null, null, null, null, null, null, null);
        ThingsCursorWrapper cursorWrapped = new ThingsCursorWrapper(cursor);

        try {
            if (cursorWrapped.getCount() == 0) {
                return null;
            }

            cursorWrapped.moveToLast();
            return cursorWrapped.getThing();
        } finally {
            cursorWrapped.close();
        }
    }


    /**
     * Search the database for a thing.
     * @param what The name of the thing
     * @return Returns the first match of Thing found in the database. If no matches
     * are found null is returned.
     */
    public Thing search(String what) {

        ThingsCursorWrapper cursor = queryThings(ThingsTable.Cols.WHAT + " LIKE ?",
                new String[]{"%"+what+"%"});

        try {
            if (cursor.getCount() == 0)
                return null;

            cursor.moveToFirst();
            return cursor.getThing();
        } finally {
            cursor.close();
        }
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
