package mmad.sjurdur.tingle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sjurdur on 27/02/16.
 *
 * Custom TextView class so that
 * we can toggle the information inside
 * an item in the list view.
 */
class ToggledTextView extends TextView {

    private boolean isToggled;

    public ToggledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isToggled = false;
    }

    public boolean isToggled() {
        return isToggled;
    }

    public void toggle() {
        this.isToggled = !isToggled;
    }
}
