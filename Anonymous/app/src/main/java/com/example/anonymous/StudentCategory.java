package com.example.anonymous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentCategory extends AppCompatActivity {

    boolean isAnxious,isGrief,isTrauma,isReport,isChallenge,isInterfere,isDepress;
    TextView anxious,grief,trauma,report,challenge,interfere,depress;
    ImageButton back;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String StudentId;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_category);

        anxious = findViewById(R.id.anxious1);
        grief = findViewById(R.id.grief1);
        trauma = findViewById(R.id.trauma1);
        report = findViewById(R.id.report1);
        challenge = findViewById(R.id.challenge1);
        interfere = findViewById(R.id.interfere1);
        depress = findViewById(R.id.depress1);
        back = findViewById(R.id.backbuttonCategory);
        StudentId = getIntent().getStringExtra("studentid").toString();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        CollectionReference usersRef = firebaseFirestore.collection("Category Details");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersRef.document(StudentId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        isAnxious = document.getBoolean("I feel anxious or overwhelmed");
                        isGrief = document.getBoolean("I am grieving");
                        isTrauma = document.getBoolean("I have experienced trauma");
                        isReport = document.getBoolean("I want to report an incident");
                        isChallenge = document.getBoolean("I need to talk through a specific challenge");
                        isInterfere = document.getBoolean("My troubles are interfering with my academic perfomance");
                        isDepress = document.getBoolean("I feel depressed");
                    }
                }
            }
        });

        if(isAnxious)
            anxious.setText("I feel anxious or overwhelmed"+"  YES");
        else
            anxious.setText("I feel anxious or overwhelmed" + "  NO");
        if(isGrief)
            grief.setText("I am grieving" + "  YES");
        else
            grief.setText("I am grieving" + "  NO");
        if(isTrauma)
            trauma.setText("I have experienced trauma" + "  YES");
        else
            trauma.setText("I have experienced trauma" + "  NO");
        if(isReport)
            report.setText("I want to report an incident" + "  YES");
        else
            report.setText("I want to report an incident" + "  NO");
        if(isChallenge)
            challenge.setText("I need to talk through a specific challenge" + "  YES");
        else
            challenge.setText("I need to talk through a specific challenge" + "  NO");
        if(isInterfere)
            interfere.setText("My troubles are interfering with my academic perfomance"+ "  YES");
        else
            interfere.setText("My troubles are interfering with my academic perfomance" + "  NO");
        if(isDepress)
            depress.setText("I feel depressed" + "  YES");
        else
            depress.setText("I feel depressed" + "  NO");


    }
}