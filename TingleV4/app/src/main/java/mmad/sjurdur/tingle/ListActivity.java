package mmad.sjurdur.tingle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListActivity extends AppCompatActivity {

    //fake database
    private static ThingsDB mThingsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mThingsDB = ThingsDB.get();

        // our adapter instance
        ArrayAdapterItem adapter = new ArrayAdapterItem(this, R.layout.list_item, mThingsDB);

        // create a new ListView, set the adapter and item click listener
        ListView listViewItems = (ListView) findViewById(R.id.list_of_items);
        listViewItems.setAdapter(adapter);
    }
}
