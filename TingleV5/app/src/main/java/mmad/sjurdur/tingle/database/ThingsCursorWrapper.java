package mmad.sjurdur.tingle.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

import mmad.sjurdur.tingle.Thing;
import mmad.sjurdur.tingle.database.ThingsDbSchema.ThingsTable;

/**
 * Created by sjurdur on 11/04/16.
 */
public class ThingsCursorWrapper extends CursorWrapper{
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public ThingsCursorWrapper(Cursor cursor) { super(cursor);}

    public Thing getThing() {
        String uuidString = getString(getColumnIndex(ThingsTable.Cols.UUID));
        UUID uuid = UUID.fromString(uuidString);
        String what = getString(getColumnIndex(ThingsTable.Cols.WHAT));
        String where = getString(getColumnIndex(ThingsTable.Cols.WHERE));

        Thing thing = new Thing(uuid, what, where);
        return thing;
    }

}
