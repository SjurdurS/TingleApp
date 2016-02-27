package mmad.sjurdur.tingle;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class ListFragment extends Fragment {

    //fake database
    private static ThingsDB mThingsDB;

    private ArrayAdapterItem adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThingsDB = ThingsDB.get();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateAdapter();
    }

    public void updateAdapter(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        mThingsDB = ThingsDB.get();

        // our adapter instance
        adapter = new ArrayAdapterItem(getActivity(), R.layout.list_item, mThingsDB);

        // create a new ListView, set the adapter and item click listener
        ListView listViewItems = (ListView) v.findViewById(R.id.list_of_items);
        listViewItems.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listViewItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Thing item = mThingsDB.getThingsDB().get(position);
                mThingsDB.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity(), "You selected : " + item, Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

}
