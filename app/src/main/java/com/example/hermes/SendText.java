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

import java.lang.reflect.Array;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendText extends Activity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String reciever;
    public String contactsFirestore = "hermes-contacts";
    public String theMessages = "messages";

    public static Object[] Dis(String t){
        Object[] objects;
        ArrayList<String> platforms = new ArrayList<String>(); // Create an ArrayList object
        ArrayList<Character> singular = new ArrayList<Character>(); // Create an ArrayList object
//        this is to converted the string returned into proper data structure
        for (int i = 0; i < t.length(); i++){
            char c = t.charAt(i);
            switch (c) {
                case ',':

                    StringBuilder builder = new StringBuilder(singular.size());
                    for(Character ch: singular)
                    {
                        builder.append(ch);
                    }
                    platforms.add(builder.toString());

                    singular.clear();
                case ']':

                    StringBuilder builder2 = new StringBuilder(singular.size());
                    for(Character ch: singular)
                    {
                        builder2.append(ch);
                    }
                    platforms.add(builder2.toString());
                    continue;
                case '[':
                    continue;
                case ' ':
                    continue;
                default:
                    singular.add(c);
            }

        }
        objects = platforms.toArray();
        return objects;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Object[] all = Dis(getIntent().getStringExtra("PLATFROMS_SELECTED"));
        StringBuilder str = new StringBuilder();
        for (Object obj : all){
            System.out.print(obj + " ");
            str.append(obj+" ");
        }

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
                        textView.setText("What would you like to send " + reciever + " on: " + str.toString() +"?");

                    } else {
                        Log.i("LOGGER", "No such document");
                    }
                } else {
                    Log.i("LOGGER", "get failed with ", task.getException());
                }
            }

        });
        Button startBtn = (Button) findViewById(R.id.buttonSend);
        startBtn.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
               Editable theText =  messageInput.getText();
                Toast.makeText(getApplicationContext(),"TEXT: '" + theText + "' has been sent!", Toast.LENGTH_LONG ).show();
                Log.d("LOGGER",  theText.toString());
                sendSMS(theText.toString());
               feedBackText.setText("MESSAGE SENT");

            }


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

//        this is to make the platform into a list
        List<String> allPlat = new ArrayList<>();
        Object[] all = Dis(getIntent().getStringExtra("PLATFROMS_SELECTED"));
        for (Object obj : all){
            System.out.print(obj + " ");
            if(obj != ""){
                allPlat.add(obj + " ");
            }

        }
        data.put("chosenPlatform", allPlat);

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

}