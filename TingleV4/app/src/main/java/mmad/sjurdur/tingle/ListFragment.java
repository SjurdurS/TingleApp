package mmad.sjurdur.tingle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListFragment extends Fragment {

    //fake database
    private static ThingsDB mThingsDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThingsDB = ThingsDB.get();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mThingsDB = ThingsDB.get();

        // our adapter instance
        ArrayAdapterItem adapter = new ArrayAdapterItem(getActivity(), R.layout.list_item, mThingsDB);

        // create a new ListView, set the adapter and item click listener
        ListView listViewItems = (ListView) v.findViewById(R.id.list_of_items);
        listViewItems.setAdapter(adapter);

        return v;
    }
}
