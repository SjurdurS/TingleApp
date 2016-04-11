package mmad.sjurdur.tingle;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

public class ListActivity extends FragmentActivity
    implements ListFragment.UpdateListFragmentListener {

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

    @Override
    public void onUpdateListFragment() {
        Log.d("ListActivity", "onUpdateListFragment called.");

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Call the FragmentManager
            FragmentManager manager = getSupportFragmentManager();

            // Collect fragment (layout)
            Fragment fragment_list = manager.findFragmentById(R.id.activity_list_fragment_container);

            // Use the manager to begin transaction, and remove the fragment
            manager.beginTransaction()
                    .remove(fragment_list)
                    .commit();

            // Create a new Fragment (ListFragment.java)
            fragment_list = new ListFragment();

            // Use the manager to begin transaction, and add the new(updated) fragment
            manager.beginTransaction()
                    .add(R.id.activity_list_fragment_container, fragment_list)
                    .commit();
        }
    }
}