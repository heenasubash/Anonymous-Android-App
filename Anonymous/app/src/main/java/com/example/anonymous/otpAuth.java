package com.example.anonymous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class otpAuth extends AppCompatActivity {

    TextView mChangeNumberText;
    EditText mgetOTP;
    android.widget.Button mVerifyOTP;
    String enteredotp;

    FirebaseAuth firebaseAuth;
    ProgressBar mprogressbarOTP;
    String PhoneNumber;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);

        mChangeNumberText = findViewById(R.id.ChangeNumberText);
        mgetOTP = findViewById(R.id.getOTP);
        mVerifyOTP = findViewById(R.id.VerifyOTP);
        mprogressbarOTP = findViewById(R.id.progressbarOTP);
        PhoneNumber = getIntent().getStringExtra("phonenumber");

        firebaseAuth = FirebaseAuth.getInstance();

        mChangeNumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(otpAuth.this,MainActivity.class);
                startActivity(intent);
            }
        });

        mVerifyOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredotp = mgetOTP.getText().toString();
                if(enteredotp.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter the OTP", Toast.LENGTH_SHORT).show();
                }
                else {
                    mprogressbarOTP.setVisibility(View.VISIBLE);
                    String codereceived = getIntent().getStringExtra("otp");
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codereceived,enteredotp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    mprogressbarOTP.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"OTP Verified successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(otpAuth.this, Register.class);
                    intent.putExtra("Phonenum",PhoneNumber).toString();
                    startActivity(intent);
                    finish();
                }
                else
                {
                   if (task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                   {
                       mprogressbarOTP.setVisibility(View.INVISIBLE);
                       Toast.makeText(getApplicationContext(),"OTP Verification failed", Toast.LENGTH_SHORT).show();
                   }
                }
            }
        });
    }
}