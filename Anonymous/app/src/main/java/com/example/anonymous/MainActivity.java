package com.example.anonymous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText mGetNum;
    android.widget.Button motpButton;
    CountryCodePicker mCodePicker;
    String countrycode;
    String phonenumber;

    FirebaseAuth firebaseAuth;
    ProgressBar mprogressbar;
    FirebaseFirestore firebaseFirestore;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    String codesent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCodePicker = findViewById(R.id.CodePicker);
        mGetNum = findViewById(R.id.GetNum);
        motpButton = findViewById(R.id.otpButton);
        mprogressbar = findViewById(R.id.progressbar);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        countrycode = mCodePicker.getSelectedCountryCodeWithPlus();

        mCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode = mCodePicker.getSelectedCountryCodeWithPlus();
            }
        });

        motpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number;
                number = mGetNum.getText().toString();
                if (number.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please enter your phone number",Toast.LENGTH_SHORT).show();
                }
                else if(number.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Please enter correct phone number",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mprogressbar.setVisibility(View.VISIBLE);
                    phonenumber = countrycode+number;

                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder()
                            .setPhoneNumber(phonenumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(MainActivity.this)
                            .setCallbacks(mcallbacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);

                }
            }
        });

        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //automatic otp fetch code should come here
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(),"OTP Sent!",Toast.LENGTH_SHORT).show();
                mprogressbar.setVisibility(View.INVISIBLE);

                codesent= s;

                Intent intent = new Intent( MainActivity.this, otpAuth.class);
                intent.putExtra("otp",codesent);
                startActivity(intent);

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        CollectionReference usersRef = firebaseFirestore.collection("Users");

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
           String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            usersRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String type = document.getString("userType");
                            if(type.equals("Student")) {
                                startActivity(new Intent(MainActivity.this, StudentChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            } else if (type.equals("Faculty")) {
                                startActivity(new Intent(MainActivity.this, FacultyChatActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }
                            else{
                                startActivity(new Intent(MainActivity.this, NonFacultyChat.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                            }
                        }
                    }
                }
            });



                /*Intent intent = new Intent(MainActivity.this, StudentChatActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);*/

        }
    }
}