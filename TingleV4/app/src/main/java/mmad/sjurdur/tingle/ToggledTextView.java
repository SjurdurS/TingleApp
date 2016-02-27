package mmad.sjurdur.tingle;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by sjurdur on 27/02/16.
 */
class ToggledTextView extends TextView {

    private boolean toggled;

    public ToggledTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        toggled = false;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void toggle() {
        this.toggled = !toggled;
    }
}
