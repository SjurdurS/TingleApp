package mmad.sjurdur.tingle;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListActivity extends FragmentActivity {

    private Fragment activity_fragment;
    private Fragment list_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        FragmentManager fm = getSupportFragmentManager();
        list_fragment = fm.findFragmentById(R.id.activity_list_fragment_container);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

            activity_fragment = fm.findFragmentById(R.id.activity_tingle_fragment_container);
            if (activity_fragment == null) {
                activity_fragment = new TingleFragment();
                fm.beginTransaction()
                        .add(R.id.activity_tingle_fragment_container, activity_fragment)
                        .commit();
            }
        }

        list_fragment = fm.findFragmentById(R.id.activity_list_fragment_container);
        if (list_fragment == null) {
            list_fragment = new ListFragment();
            fm.beginTransaction()
                    .add(R.id.activity_list_fragment_container, list_fragment)
                    .commit();
        }

    }

}