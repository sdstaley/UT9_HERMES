package com.example.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

public class ServicesActivity extends AppCompatActivity {

    // Array of strings...
    String[] mobileArray = {"Text","Email","Facebook","Instagram",
            "Twitter","WhatsApp","Discord","Slack"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_listview);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, mobileArray);

        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        // Listener function checking to see if any of the services are clicked on by user.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchActivities2();
            }
        });

        // This is for the back button.
        View switchToSecondActivity = findViewById(R.id.back_button1);
        switchToSecondActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities();
            }
        });
    }
    // Switch activity to go to the main activity page, aka 'MainActivity' class
    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    // Switch activity to go to the messaging, 'SendText' class
    private void switchActivities2() {
        Intent switchActivityIntent = new Intent(this, SendText.class);
        startActivity(switchActivityIntent);
    }
}
