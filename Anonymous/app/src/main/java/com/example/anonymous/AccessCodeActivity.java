package com.example.anonymous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AccessCodeActivity extends AppCompatActivity {

    android.widget.Button mallow,mnoaccess;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Intent intent;
    String studentid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_code);

        mallow = findViewById(R.id.allow);
        mnoaccess = findViewById(R.id.noaccess);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        studentid = getIntent().getStringExtra("studentid");

        mallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference documentReference = firebaseFirestore.collection("Users").document(studentid);

                documentReference.update("Access",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "This Student now has Non Faculty Access", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });

            }
        });

        mnoaccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DocumentReference documentReference = firebaseFirestore.collection("Users").document(studentid);

                documentReference.update("Access",false).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "This Student now has Non Faculty Access", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });

            }
        });

    }
}