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

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private ArrayAdapter adapter;
    private ListView contactsListView;
    private List<String> list;
    // Firebase Fields
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    public String contactsFirestore = "hermes-contacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
//
//        db.collection("hermes-contacts")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });


        //Get document names from collection
        db.collection(contactsFirestore).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, list.toString());
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getString("name"));
                    }
                        adapter = new ArrayAdapter<String>(MainActivity.this.getApplicationContext(), android.R.layout.simple_list_item_1,  list);
                        contactsListView = (ListView) findViewById(R.id.messagingServicesListView);
                        contactsListView.setAdapter(adapter);
                        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                switchActivities();
                            }
                        });

                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }

            }
        });
    }

    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, ServicesActivity.class);
        startActivity(switchActivityIntent);
    }
}
