package com.example.hermes;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.view.View;

public class ServicesActivity extends Activity{

    // Array of strings...
    String[] mobileArray = {"Text","Email","Facebook","Instagram",
            "Twitter","WhatsApp","Discord","Slack"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                Toast.makeText(getApplicationContext(), "long clicked", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }
}
