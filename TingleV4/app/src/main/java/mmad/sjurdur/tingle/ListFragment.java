package mmad.sjurdur.tingle;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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
        updateListView();
    }

    public void updateListView(){
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list, container, false);

        // our adapter instance
        adapter = new ArrayAdapterItem(getActivity(), R.layout.list_item, mThingsDB);

        // create a new ListView, set the adapter and item click listener
        ListView listViewItems = (ListView) v.findViewById(R.id.list_of_items);
        listViewItems.setAdapter(adapter);


        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        listViewItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> av, View v, int position, long id) {
                //Get your item here with the position
                Thing thing = mThingsDB.get(position);
                MyOnClickListener myOnClickListener = new MyOnClickListener(position);
                builder.setMessage("Are you sure you want to delete the following? \n" +
                                    "Thing:\t" + thing.getWhat().toString() + "\n" +
                                    "location:\t" + thing.getWhere())
                        .setPositiveButton("Yes", myOnClickListener)
                        .setNegativeButton("No", myOnClickListener)
                        .show();
                return true;
            }
        });

        listViewItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View rowView, int position, long id) {
                Thing thing = mThingsDB.getThingsDB().get(position);

                ToggledTextView textView = (ToggledTextView) rowView.findViewById(R.id.textViewItem);

                // Toggle What / Where on click
                if (textView.isToggled()) {
                    textView.setText(thing.getWhat());
                } else {
                    textView.setText("Location: " + thing.getWhere());
                }
                textView.toggle();
            }
        });

        return v;
    }

    private class MyOnClickListener implements DialogInterface.OnClickListener {

        int mPositionOfClickedThing;

        public MyOnClickListener(int positionOfClickedThing) {
            this.mPositionOfClickedThing = positionOfClickedThing;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    mThingsDB.remove(mPositionOfClickedThing);
                    updateListView();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Do nothing
                    break;
            }
        }
    }
}
