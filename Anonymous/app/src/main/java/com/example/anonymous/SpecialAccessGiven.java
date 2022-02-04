package com.example.anonymous;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


import java.util.ArrayList;
import java.util.Calendar;


public class SpecialAccessGiven extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth firebaseAuth;
    ImageButton mbackbutton,mschedule;
    int hours, min, day, month, year,hours1;
    int min1;
    String mTitle, mDescr;

    FirestoreRecyclerAdapter<firebasemodel, SpecialAccessGiven.NoteViewHolder> chatAdapter;

    RecyclerView mrecyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_access_given);

        mrecyclerview=findViewById(R.id.recyclerview);
        mbackbutton = findViewById(R.id.backButtonspecial);
        mschedule = findViewById(R.id.scheduleStudent);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore= FirebaseFirestore.getInstance();
        CollectionReference usersRef = firebaseFirestore.collection("Schedule");

        mbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        day = document.getLong("Day").intValue();
                        month = document.getLong("Month").intValue();
                        year = document.getLong("Year").intValue();
                        hours = document.getLong("HoursStart").intValue();
                        min = document.getLong("MinutesStart").intValue();
                        hours1 = document.getLong("HoursEnd").intValue();
                        min1 = document.getLong("MinutesEnd").intValue();
                        mTitle = document.getString("Title");
                        mDescr = document.getString("Description");

                    }
                }
            }
        });

        mschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar beginTime = Calendar.getInstance();
                beginTime.set(year, month, day, hours, min);

                Calendar endTime = Calendar.getInstance();
                endTime.set(year, month, day, hours1, min1);

                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                                beginTime.getTimeInMillis())
                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                                endTime.getTimeInMillis())
                        .putExtra(CalendarContract.Events.TITLE, mTitle)
                        .putExtra(CalendarContract.Events.DESCRIPTION, mDescr)
                        //.putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
                        .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY);
                //.putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");

                startActivity(intent);
            }
        });





        Query query=firebaseFirestore.collection("Users").whereEqualTo("userType","NonFaculty");
        FirestoreRecyclerOptions<firebasemodel> allusername=new FirestoreRecyclerOptions.Builder<firebasemodel>().setQuery(query,firebasemodel.class).build();

        chatAdapter=new FirestoreRecyclerAdapter<firebasemodel, SpecialAccessGiven.NoteViewHolder>(allusername) {
            @Override
            protected void onBindViewHolder(@NonNull SpecialAccessGiven.NoteViewHolder noteViewHolder, int i, @NonNull firebasemodel firebasemodel) {

                noteViewHolder.particularusername.setText(firebasemodel.getName());
                if(firebasemodel.getStatus().equals("Online"))
                {
                    noteViewHolder.statusofuser.setText(firebasemodel.getStatus());
                    noteViewHolder.statusofuser.setTextColor(Color.GREEN);
                }
                else
                {
                    noteViewHolder.statusofuser.setText(firebasemodel.getStatus());
                }

                noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getApplicationContext(),SpecificChat.class);
                        intent.putExtra("name",firebasemodel.getName());
                        intent.putExtra("receiveruid",firebasemodel.getUid());
                        startActivity(intent);
                    }
                });



            }

            @NonNull
            @Override
            public SpecialAccessGiven.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.chatviewlayout,parent,false);
                return new SpecialAccessGiven.NoteViewHolder(view);
            }
        };


        mrecyclerview.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mrecyclerview.setLayoutManager(linearLayoutManager);
        mrecyclerview.setAdapter(chatAdapter);


    }


    public class NoteViewHolder extends RecyclerView.ViewHolder
    {

        private TextView particularusername;
        private TextView statusofuser;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            particularusername=itemView.findViewById(R.id.nameofuser);
            statusofuser=itemView.findViewById(R.id.statusofuser);

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        chatAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(chatAdapter!=null)
        {
            chatAdapter.stopListening();
        }
    }
}