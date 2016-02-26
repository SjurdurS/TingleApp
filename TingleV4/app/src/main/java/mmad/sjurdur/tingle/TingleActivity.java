package mmad.sjurdur.tingle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class TingleActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tingle);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.activity_tingle_fragment_container);

        if (fragment == null) {
            fragment = new TingleFragment();
            fm.beginTransaction()
                    .add(R.id.activity_tingle_fragment_container, fragment)
                    .commit();
        }

    }

}