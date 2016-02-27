package mmad.sjurdur.tingle;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

public class TingleActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tingle);

        FragmentManager fm = getSupportFragmentManager();
        Fragment activity_fragment = fm.findFragmentById(R.id.activity_tingle_fragment_container);
        Fragment list_fragment = fm.findFragmentById(R.id.activity_list_fragment_container);

        if (activity_fragment == null) {
            activity_fragment = new TingleFragment();
            fm.beginTransaction()
                    .add(R.id.activity_tingle_fragment_container, activity_fragment)
                    .commit();
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (list_fragment == null) {
                list_fragment = new ListFragment();
                fm.beginTransaction()
                        .add(R.id.activity_list_fragment_container, list_fragment)
                        .commit();
            }
        }
    }

}