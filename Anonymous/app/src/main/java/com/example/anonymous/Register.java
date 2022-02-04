package com.example.anonymous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Register extends AppCompatActivity {

    private EditText mGetName;
    private android.widget.Button mRegister;
    private String name;
    private RadioGroup mradiobutton;
    private String userType;
    private String unique = UUID.randomUUID().toString();
    private RadioButton mStudentRadio;
    private RadioButton mFacultyRadio;
    private RadioButton mNonFacultyRadio;

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    ProgressBar mprogressbarRegister;
    String PhoneNum = getIntent().getStringExtra("Phonenum");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mGetName = findViewById(R.id.GetName);
        mRegister = findViewById(R.id.Register);
        mprogressbarRegister = findViewById(R.id.progressbarRegister);
        mradiobutton = findViewById(R.id.radiobutton);
        mStudentRadio = findViewById(R.id.StudentRadio);
        mFacultyRadio = findViewById(R.id.FacultyRadio);
        mNonFacultyRadio = findViewById(R.id.NonFacultyRadio);

        mStudentRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userType = "Student";
                mGetName.setText(unique);
            }
        });

        mFacultyRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userType = "Faculty";
                mGetName.setText("");

            }
        });

        mNonFacultyRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userType = "NonFaculty";
                mGetName.setText("");

            }
        });



        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userType.equals("Faculty") || userType.equals("NonFaculty" )|| userType.equals("Student")){
                    name = mGetName.getText().toString();
                }
                if(name.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter a name",Toast.LENGTH_SHORT).show();
                }
                else
                {

                    mprogressbarRegister.setVisibility(View.VISIBLE);
                    sendDataForNewUser();
                    sendDataTocloudFirestore();
                    mprogressbarRegister.setVisibility(View.INVISIBLE);
                    if (userType.equals("Student")) {
                        Intent intent = new Intent(Register.this, StudentChatActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if(userType.equals("Faculty")){
                        Intent intent = new Intent(Register.this, FacultyChatActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if(userType.equals("NonFaculty")){
                        Intent intent = new Intent(Register.this, NonFacultyChat.class);
                        startActivity(intent);
                        finish();
                    }


                }
            }
        });

    }
   private void sendDataForNewUser(){
        sendDataToRealTimeDatabase();
    }

    private void sendDataToRealTimeDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://anonymous-9454d-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference databaseReference = firebaseDatabase.getReference(firebaseAuth.getUid());
        if (userType.equals("Student")){
            userprofile muserprofile = new userprofile(unique,firebaseAuth.getUid());
            databaseReference.setValue(muserprofile);
        }
        else{
            userprofile muserprofile = new userprofile(name,firebaseAuth.getUid());
            databaseReference.setValue(muserprofile);
        }
        Toast.makeText(getApplicationContext(),"User Profile Added Successfully",Toast.LENGTH_SHORT).show();

    }

        


    private void sendDataTocloudFirestore() {


        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String, Object> userdata = new HashMap<>();
        if (userType.equals("Student")) {
            userdata.put("name", unique);
        } else {
            userdata.put("name", name);
        }
        userdata.put("userType",userType);
        userdata.put("uid", firebaseAuth.getUid());
        userdata.put("status", "Online");
        userdata.put("PhoneNumber", PhoneNum);
        userdata.put("Access",null);

        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Data on Cloud Firestore send success", Toast.LENGTH_SHORT).show();

            }
        });



    }
}