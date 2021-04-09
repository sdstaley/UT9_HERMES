package com.example.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    private static String TAG = "ServicesActivity";
    public String contactsFirestore = "hermes-contacts";
    public String messagingServicesFirestore = "personal-messagingservices";
    private List<String> list;
    private ArrayAdapter adapter;
    private ListView messagingServicesListView;
    public Task<QuerySnapshot> task;


    // Firebase Fields
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_listview);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        list = new ArrayList<>();

        //Get messaging services document from each contact
        db
               .collection(contactsFirestore)
               .document("0fwpEjkp6gSNfTlrO7s2")
               .collection(messagingServicesFirestore).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, list.toString());
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // update to fix array issue. angela's services.
                                //list.add(document.get("used-services").toString());
                                list.add(document.getString("1"));
                                list.add(document.getString("2"));
                                list.add(document.getString("3"));
                            }
                            adapter = new ArrayAdapter<String>(ServicesActivity.this.getApplicationContext(), android.R.layout.simple_list_item_1,  list);
                            messagingServicesListView = (ListView) findViewById(R.id.messagingServicesListView);
                            messagingServicesListView.setAdapter(adapter);
                            messagingServicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    switchActivities2();
                                }
                            });

                        } else {
                            Log.e(TAG, "Error getting documents: ", task.getException());
                        }

                    }
                });;



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
