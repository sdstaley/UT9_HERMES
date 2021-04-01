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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "FROM FIREBASE";

    // Firebase Fields
//    List<String> DocumentNamesFromFirebase = new ArrayList<>();
//    List<String> Names = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    int objectLength;
    Object[] DocumentNames;
    private String CollectionName = "hermes-contacts";
    private String DocumentName = "0fwpEjkp6gSNfTlrO7s2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<String> list = new ArrayList<>();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //        getDocumentFromFirebase();
        DocumentReference docRef = db.collection(CollectionName).document(DocumentName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.i("LOGGER", document.getString("name"));
                    } else {
                        Log.i("LOGGER", "No such document");
                    }
                } else {
                    Log.i("LOGGER", "get failed with ", task.getException());
                }
            }

        });

        //Get document names from collection
        db.collection(CollectionName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());
                        Log.i("LOGGER", list.toString());
                    }
                } else {
                    Log.i(TAG, "Error getting documents: ", task.getException());
                }
                objectLength = list.size();
                DocumentNames = new String[objectLength];
                DocumentNames = list.toArray();
                for (int i = 1; i <= DocumentNames.length-1; i++){
                    DocumentNames[i-1] = list.get(i-1);
                    Log.i("LOGGER2", (String) DocumentNames[i-1]);

                }
            }
        });

        // Creates a list of strings with names of each contact.
        String[] mobileArray = {"Michael","Jim","Pam","Dwight", "Ryan","Angela","Kevin","Corporate"};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
//       android.R.layout.simple_list_item_1, Names);
        R.layout.activity_listview, mobileArray);
        ListView listView = (ListView) findViewById(R.id.mobile_list);
        listView.setAdapter(adapter);

        // Just a listener function that listens for the user's click.
        // If it happens, then we call switchActivities function.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchActivities();
            }
        });
    }
    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, ServicesActivity.class);
        startActivity(switchActivityIntent);
    }
}
