package com.example.hermes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private ArrayAdapter adapter;
    private ListView contactsListView;
    private List<String> list;
    private Map<String, String> viewList ;
    private Toolbar toolbar;
    private Button signoutButton;


    // Firebase Fields
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    public String contactsFirestore = "hermes-contacts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbarMA);
        setSupportActionBar(toolbar);

        signoutButton = findViewById(R.id.buttonSignOut);
        dialogSignOut(signoutButton);

        list = new ArrayList<>();
        viewList = new HashMap<>();


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Get document names from collection
        db.collection(contactsFirestore).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, list.toString());
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getString("name"));
                        viewList.put(document.getString("name"), document.getId());
                    }
                        adapter = new ArrayAdapter<String>(MainActivity.this.getApplicationContext(), android.R.layout.simple_list_item_1,  list);
                        contactsListView = (ListView) findViewById(R.id.messagingServicesListView);
                        contactsListView.setAdapter(adapter);
                        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                String clickedItem = (String) (parent.getItemAtPosition(position));
                                String documentName = viewList.get(clickedItem);
                                switchActivities(documentName);
//                                Toast.makeText(getApplicationContext(), "You clicked on " + documentName, Toast.LENGTH_LONG ).show();
                            }
                        });

                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }

            }
        });
    }

    private void dialogSignOut(View view){
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setMessage("Are you sure you want to sign out?").setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        logOut();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                           dialogInterface.cancel();
                        }
                    });
                AlertDialog alertDialog = ad.create();
                alertDialog.setTitle("Hermes");
                alertDialog.show();
            }
        });
    }

    private void logOut() {
        Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(logoutIntent);
        finish();
    }


    private void switchActivities( String documentNameParameter ) {
        Intent switchActivityIntent = new Intent(MainActivity.this, ServicesActivity.class);
        switchActivityIntent.putExtra("NAME", documentNameParameter);

        startActivity(switchActivityIntent);
    }
}
