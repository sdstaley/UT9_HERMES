package com.example.hermes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
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

public class MainActivity extends AppCompatActivity {
    private static String TAG = "FROM FIREBASE";

    // Firebase Fields
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String CollectionName = "hermes-contacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<String> list = new ArrayList<>();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Get document names from collection
        db.collection(CollectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getString("name"));
                    }
                } else {
                    Log.i(TAG, "Error getting documents: ", task.getException());
                }
                        Log.i("LOGGER", list.toString());
                        ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this.getApplicationContext(), android.R.layout.simple_list_item_1,  list);
                        ListView listView = (ListView) findViewById(R.id.mobile_list);
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switchActivities();
                    }
                });
            }
        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, ServicesActivity.class);
        startActivity(switchActivityIntent);
    }
}
