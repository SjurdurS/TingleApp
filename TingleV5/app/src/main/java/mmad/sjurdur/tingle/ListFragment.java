package mmad.sjurdur.tingle;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.UUID;

public class ListFragment extends Fragment {
    UpdateListFragmentListener mCallback;

    /**
     * The container Activity must implement this interface so the frag can deliver messages
     *
     * This is based on the Android tutorial on Fragment Communication:
     * http://developer.android.com/training/basics/fragments/communicating.html
     */
    public interface UpdateListFragmentListener {

        /** Called by a fragment when a list item is added or deleted */
        void onUpdateListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        if (context instanceof ListActivity || context instanceof TingleActivity){
            try {
                mCallback = (UpdateListFragmentListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement UpdateListFragmentListener");
            }
        }
    }
    
    private static ThingsDB mThingsDB;

    private ArrayAdapterItem adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mThingsDB = ThingsDB.get(getActivity());
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
                Thing thing = (Thing) av.getItemAtPosition(position);
                MyOnClickListener myOnClickListener = new MyOnClickListener(thing);
                builder.setMessage("Are you sure you want to delete the following? \n" +
                                    "Thing:\t\t\t" + thing.getWhat().toString() + "\n" +
                                    "Location:\t" + thing.getWhere())
                        .setPositiveButton("Yes", myOnClickListener)
                        .setNegativeButton("No", myOnClickListener)
                        .show();
                return true;
            }
        });

        listViewItems.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View rowView, int position, long id) {
                Thing thing = (Thing) parent.getItemAtPosition(position);

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

        Thing mThing;

        public MyOnClickListener(Thing thing) {
            this.mThing = thing;
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    boolean removed = mThingsDB.remove(mThing);
                    if (removed);
                        Log.d("MyOnClickListener", "thing deleted.");
                    mCallback.onUpdateListFragment();
                    updateListView();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    // Do nothing
                    break;
            }
        }
    }
}
