package com.example.anonymous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CategoryActivity extends AppCompatActivity {

    CheckBox anxious,grief,trauma,report,challenge,interfere,depress;
    boolean isAnxious,isGrief,isTrauma,isReport,isChallenge,isInterfere,isDepress;
    android.widget.Button catButton;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        anxious = findViewById(R.id.anxious);
        grief = findViewById(R.id.grief);
        trauma = findViewById(R.id.trauma);
        report = findViewById(R.id.report);
        challenge = findViewById(R.id.challenge);
        interfere = findViewById(R.id.interfere);
        depress = findViewById(R.id.depress);
        catButton = findViewById(R.id.catButton);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        anxious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(anxious.isChecked())
                   isAnxious = true;
            }
        });

        grief.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(grief.isChecked())
                    isGrief = true;
            }
        });

        trauma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(trauma.isChecked())
                   isTrauma = true;
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(report.isChecked())
                   isReport = true;
            }
        });

        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(challenge.isChecked())
                   isChallenge = true;
            }
        });

        interfere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(interfere.isChecked())
                   isInterfere = true;
            }
        });

        depress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(depress.isChecked())
                   isDepress = true;
            }
        });

        catButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToCloudFirestore();
                Intent intent = new Intent(CategoryActivity.this,StudentChatActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void sendDataToCloudFirestore() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firebaseFirestore.collection("Category Details").document(firebaseAuth.getUid());
        Map<String, Object> userdata = new HashMap<>();
        if(isAnxious)
            userdata.put("I feel anxious or overwhelmed",true);
        else
            userdata.put("I feel anxious or overwhelmed",false);
        if(isGrief)
            userdata.put("I am grieving",true);
        else
            userdata.put("I am grieving",false);
        if(isTrauma)
            userdata.put("I have experienced trauma",true);
        else
            userdata.put("I have experienced trauma",false);
        if(isReport)
            userdata.put("I want to report an incident",true);
        else
            userdata.put("I want to report an incident",false);
        if(isChallenge)
            userdata.put("I need to talk through a specific challenge",true);
        else
            userdata.put("I need to talk through a specific challenge",false);
        if(isInterfere)
            userdata.put("My troubles are interfering with my academic perfomance",true);
        else
            userdata.put("My troubles are interfering with my academic perfomance",false);
        if(isDepress)
            userdata.put("I feel depressed",true);
        else
            userdata.put("I feel depressed",false);

        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Category Data Saved!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}