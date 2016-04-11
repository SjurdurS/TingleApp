package mmad.sjurdur.tingle;

import java.io.Console;
import java.util.Comparator;
import java.util.Objects;

/**
 * Created by sjurdur on 04/02/16.
 *
 * Class representing a single thing.
 */
public class Thing {
    private String mWhat  = null;
    private String mWhere = null;

    public Thing(String what, String where) {
        mWhat  = what;
        mWhere = where;
    }

    @Override
    public String toString() {
        return oneLine("Item: ", "is here: ");
    }

    public String getWhat() {
        return mWhat;
    }

    public void setWhat(String what) {
        mWhat = what;
    }

    public String getWhere() {
        return mWhere;
    }

    public void setWhere(String where) {
        mWhere = where;
    }

    public String oneLine(String pre, String post) {
        return pre + mWhat + " " + post + mWhere;
    }

    @Override
    public int hashCode() {
        return mWhat.hashCode() + mWhere.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Thing))
            return false;
        if (obj == this)
            return true;

        Thing rhs = (Thing) obj;
        return ((Thing) obj).getWhat().equalsIgnoreCase(rhs.mWhat);
    }
}
