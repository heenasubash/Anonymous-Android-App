package com.example.anonymous;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import java.util.UUID;

public class Register extends AppCompatActivity {

    private EditText mGetName;
    private android.widget.Button mRegister;
    private String name;
    private RadioGroup mradiobutton;
    private String userType;
    private String unique = UUID.randomUUID().toString();

    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;

    ProgressBar mprogressbarRegister;

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

        mradiobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean checked = ((RadioButton) v).isChecked();


                switch(v.getId()) {
                    case R.id.StudentRadio:
                        if (checked) {
                            userType = "Student";
                            mGetName.setText(unique);
                            break;
                        }
                    case R.id.FacultyRadio:
                        if (checked)
                            userType = "Faculty";
                            break;
                    case R.id.NonFacultyRadio:
                        if (checked)
                            userType = "NonFaculty";
                            break;
                }

            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userType == "Student"){
                    mGetName.setText(unique);
                }
                else{
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
                    Intent intent=new Intent(Register.this,StudentChatActivity.class);
                    startActivity(intent);
                    finish();


                }
            }
        });

    }
    private void sendDataForNewUser() {
        sendDataToRealTimeDatabase();
    }

    private void sendDataToRealTimeDatabase() {
        name=mGetName.getText().toString().trim();
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference databaseReference=firebaseDatabase.getReference(firebaseAuth.getUid());
        if (userType == "Student"){
            userprofile muserprofile = new userprofile(unique,firebaseAuth.getUid());
            databaseReference.setValue(muserprofile);
        }
        else{
            userprofile muserprofile = new userprofile(name,firebaseAuth.getUid());
            databaseReference.setValue(muserprofile);
        }
        Toast.makeText(getApplicationContext(),"User Profile Added Sucessfully",Toast.LENGTH_SHORT).show();

    }

    private void sendDataTocloudFirestore() {


        DocumentReference documentReference = firebaseFirestore.collection("Users").document(firebaseAuth.getUid());
        Map<String, Object> userdata = new HashMap<>();
        if (userType == "Student") {
            userdata.put("name", unique);
        } else {
            userdata.put("name", name);
        }
        userdata.put("uid", firebaseAuth.getUid());
        userdata.put("status", "Online");

        documentReference.set(userdata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Data on Cloud Firestore send success", Toast.LENGTH_SHORT).show();

            }
        });

    }
}