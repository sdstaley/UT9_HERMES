package com.example.hermes;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.DocumentTransform;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SendText extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String reciever;
    public String contactsFirestore = "hermes-contacts";
    public String theMessages = "messages";
    DateTimeFormatter dtf ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Toast.makeText(getApplicationContext(),"TEXT" + getIntent().getStringExtra("RECEIVER"), Toast.LENGTH_LONG).show();
        setContentView(R.layout.text_message);
        TextView textView = (TextView) findViewById(R.id.platformToSendMessage);
        TextView feedBackText = (TextView) findViewById(R.id.messageState);

        EditText messageInput = (EditText) findViewById(R.id.editTextMessage);

        DocumentReference docRef = db.collection(contactsFirestore).document(getIntent().getStringExtra("RECEIVER"));
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        Log.i("LOGGER", document.getString("name"));
                        reciever = document.getString("name");
                        textView.setText("What would you like to send " + reciever + " on:" + getIntent().getStringExtra("PLATFROMS_SELECTED") +"?");

                    } else {
                        Log.i("LOGGER", "No such document");
                    }
                } else {
                    Log.i("LOGGER", "get failed with ", task.getException());
                }
            }

        });
//        textView.setText("What would you like to send " + reciever + " on:" + getIntent().getStringExtra("PLATFROMS_SELECTED") +"?");
        Button startBtn = (Button) findViewById(R.id.buttonSend);
        startBtn.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
               Editable theText =  messageInput.getText();
                Toast.makeText(getApplicationContext(),"TEXT" + theText, Toast.LENGTH_LONG).show();
                Log.d("LOGGER",  theText.toString());
                sendSMS(theText.toString());
//                messageInput.setText("");
//                feedBackText.setText("MESSAGE SENT");

            }
            /*
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendSMS();
            }

             */

        });
    }

    public void sendSMS(String text){
        TextView feedBackText = (TextView) findViewById(R.id.messageState);
        EditText messageInput = (EditText) findViewById(R.id.editTextMessage);


        Date date = new Date();
        String theSender = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Map<String, Object> data = new HashMap<>();
        data.put("msgText", text);
        data.put("recieptTime", date);
//        I hardcoded this for simplicity,
        data.put("sender","john_silver@gmail.com");
        data.put("chosenPlatform", Arrays.asList(getIntent().getStringExtra("PLATFROMS_SELECTED")));
        db
                .collection(contactsFirestore)
                .document(getIntent().getStringExtra("RECEIVER"))
                .collection(theMessages).add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("LOGGER", "DocumentSnapshot written with ID: " + documentReference.getId());
                        feedBackText.setText("MESSAGE SENT");
                        messageInput.setText("");


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("LOGGER", "Error adding document", e);
                        feedBackText.setText("ERROR SENDING MESSAGE SENT");
                    }
                });

    }
    /*

    This is Sean's work... Not finished but is a start.


    protected void sendSMS() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , new String ("01234"));
        smsIntent.putExtra("sms_body"  , "Test ");

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendText.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


     */

}