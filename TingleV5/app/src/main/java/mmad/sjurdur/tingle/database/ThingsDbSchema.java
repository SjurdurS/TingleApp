package mmad.sjurdur.tingle.database;

/**
 * Created by sjurdur on 11/04/16.
 */
public class ThingsDbSchema {

    public static final class ThingsTable {
        public static final String NAME = "things";
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String WHAT = "what";
            public static final String WHERE = "whereIs";
            // public static final String BARCODE = "barcode"; // Not used yet
        }
    }
}
