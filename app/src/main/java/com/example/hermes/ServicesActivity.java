package com.example.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ServicesActivity extends AppCompatActivity {
String MessagingPlatform = "personal-messagingservices";
    // Array of strings...
    String[] mobileArray = {"Text","Email","Facebook","Instagram",
            "Twitter","WhatsApp","Discord","Slack"};

    // Firebase Fields
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private static String TAG = "FROM FIREBASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<String> list = new ArrayList<>();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        //Get document names from collection
        db.collection(MessagingPlatform).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getString("platform"));
                    }
                } else {
                    Log.i(TAG, "Error getting documents: ", task.getException());
                }
                Log.i("LOGGER", list.toString());
                ArrayAdapter adapter = new ArrayAdapter<String>(ServicesActivity.this.getApplicationContext(), android.R.layout.simple_list_item_1,  list);
                ListView listView = (ListView) findViewById(R.id.mobile_list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switchActivities2();
                    }
                });
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_listview);
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
