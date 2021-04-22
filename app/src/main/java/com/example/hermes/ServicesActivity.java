package com.example.hermes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.List;

public class ServicesActivity extends AppCompatActivity {

    private static String TAG = "ServicesActivity";
    public String contactsFirestore = "hermes-contacts";
    public String messagingServicesFirestore = "personal-messagingservices";
    String item;
    String NameOfReceiver;
    private List<String> list;
    private List<String> itemList;
    private ArrayAdapter adapter;
    private ListView messagingServicesListView;
    public Task<QuerySnapshot> task;
    private Toolbar toolbar;
    private Button signoutButton;


    // Firebase Fields
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.services_listview);

        toolbar = findViewById(R.id.toolbarSA);
        setSupportActionBar(toolbar);

        signoutButton = findViewById(R.id.buttonSignOut);
        dialogSignOut(signoutButton);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        list = new ArrayList<>();
        itemList = new ArrayList<>();
        NameOfReceiver = getIntent().getStringExtra("NAME");


        //Get messaging services document from each contact
        db
                .collection(contactsFirestore)
                .document(getIntent().getStringExtra("NAME"))
                .collection(messagingServicesFirestore).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i(TAG, list.toString());
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getString("platform"));
                    }
                    adapter = new ArrayAdapter<String>(ServicesActivity.this.getApplicationContext(), android.R.layout.simple_list_item_multiple_choice,  list);
                    messagingServicesListView = (ListView) findViewById(R.id.messagingServicesListView);
                    messagingServicesListView.setAdapter(adapter);
                    messagingServicesListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    messagingServicesListView.setItemChecked(0, false);
                    messagingServicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            SparseBooleanArray checkedPositions = messagingServicesListView.getCheckedItemPositions();
                            if (checkedPositions != null) {
                                for (int i=0; i<checkedPositions.size(); i++) {
                                    if (checkedPositions.valueAt(i)) {
                                        item = messagingServicesListView.getAdapter().getItem(
                                                checkedPositions.keyAt(i)).toString();
                                        Log.i(TAG,item + " was selected");
                                        if(!itemList.contains(item))
                                            itemList.add(item);
                                    }else{
                                        item = messagingServicesListView.getAdapter().getItem(
                                                checkedPositions.keyAt(i)).toString();
                                        Log.i(TAG,item + " is  not selected");
                                        if(itemList.contains(item))
                                            itemList.remove(item);

                                    }
                                }
                                Log.i(TAG,itemList.toString() + " are all selected");
                            }
//                            switchActivities2();
                        }
                    });
                } else {
                    Log.e(TAG, "Error getting documents: ", task.getException());
                }
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

        View switchToSendMessageActivity = findViewById(R.id.send_message_button1);
        switchToSendMessageActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchActivities2();
            }
        });
    }
    private void dialogSignOut(View view){
        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ad = new AlertDialog.Builder(ServicesActivity.this);
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
        Intent logoutIntent = new Intent(ServicesActivity.this, LoginActivity.class);
        startActivity(logoutIntent);
        finish();
    }

    // Switch activity to go to the main activity page, aka 'MainActivity' class
    private void switchActivities() {
        Intent switchActivityIntent = new Intent(this, MainActivity.class);
        startActivity(switchActivityIntent);
    }

    // Switch activity to go to the messaging, 'SendText' class
    private void switchActivities2() {
        Intent switchActivityIntent = new Intent(this, SendText.class);
        switchActivityIntent.putExtra("PLATFROMS_SELECTED", itemList.toString());
        switchActivityIntent.putExtra("RECEIVER", NameOfReceiver);
        startActivity(switchActivityIntent);
    }
}


