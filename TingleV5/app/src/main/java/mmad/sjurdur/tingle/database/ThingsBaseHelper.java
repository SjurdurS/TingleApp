package mmad.sjurdur.tingle.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mmad.sjurdur.tingle.database.ThingsDbSchema.ThingsTable;

/**
 * Created by sjurdur on 11/04/16.
 */
public class ThingsBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "thingsBase.db";

    public ThingsBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ThingsTable.NAME + "(" +
            " _id integer primary key autoincrement, " +
            ThingsTable.Cols.UUID + ", " +
            ThingsTable.Cols.WHAT + ", " +
            ThingsTable.Cols.WHERE +
            ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
