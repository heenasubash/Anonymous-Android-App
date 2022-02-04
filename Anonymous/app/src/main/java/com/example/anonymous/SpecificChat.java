package com.example.anonymous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SpecificChat extends AppCompatActivity {

    EditText msendmessage;
    ImageButton msendbuton;
    CardView msendmessagecardview;
    androidx.appcompat.widget.Toolbar mtoolbar2;
    TextView mnameofspecificuser;
    private String enteredmessage;
    Intent intent;
    String mreceivername,sendername,mreceiveruid,msenderuid;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    String senderRoom, receiverRoom;
    ImageButton mbackbuttonofspecificchat;
    RecyclerView messagerecyclerview;
    String currenttime;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    MessageAdapter messagesAdapter;
    ArrayList<messages> messagesArrayList;
    RecyclerView mmessagerecyclerview;
    FirebaseFirestore firebaseFirestore;
    ImageButton maccessButtonnew,mvideocall,mschedule;
    String PhoneNumber1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_chat);

        msendmessage = findViewById(R.id.getmessage);
        msendmessagecardview = findViewById(R.id.cardviewofsendmessage);
        msendbuton = findViewById(R.id.imageviewsendmessage);
        mtoolbar2 = findViewById(R.id.toolbarofspecificchat);
        mnameofspecificuser = findViewById(R.id.Nameofspecificuser);
        mbackbuttonofspecificchat=findViewById(R.id.backbuttonofspecificchat);
        maccessButtonnew = findViewById(R.id.accessButtonnew);
        mvideocall = findViewById(R.id.videocall);
        mschedule = findViewById(R.id.schedule);
        intent = getIntent();
        setSupportActionBar(mtoolbar2);
        firebaseFirestore = FirebaseFirestore.getInstance();
        int permissionsCode = 42;
        String[] permissions = {Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE};
        ActivityCompat.requestPermissions(this, permissions, permissionsCode);

        //context = context.getApplicationContext();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://anonymous-9454d-default-rtdb.asia-southeast1.firebasedatabase.app");
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm:a");

        messagesArrayList=new ArrayList<>();
        mmessagerecyclerview=findViewById(R.id.recyclerviewofspecific);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagerecyclerview.setLayoutManager(linearLayoutManager);
        messagesAdapter=new MessageAdapter(SpecificChat.this,messagesArrayList);
        mmessagerecyclerview.setAdapter(messagesAdapter);

        msenderuid = firebaseAuth.getUid();
        mreceiveruid = getIntent().getStringExtra("receiveruid");
        mreceivername = getIntent().getStringExtra("name");

        senderRoom = msenderuid+mreceiveruid;
        receiverRoom = mreceiveruid+msenderuid;
        CollectionReference usersRef = firebaseFirestore.collection("Users");

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersRef.document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String type = document.getString("userType");
                        boolean access = document.getBoolean("Access");
                        if(type.equals("Faculty")) {
                            maccessButtonnew.setVisibility(View.VISIBLE);
                        }
                        if(type.equals("NonFaculty")){
                            mvideocall.setVisibility(View.VISIBLE);
                            mschedule.setVisibility(View.VISIBLE);
                        }
                        if(access){
                            mvideocall.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });

        usersRef.document(mreceiveruid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        PhoneNumber1= document.getString("PhoneNumber");
                    }
                }
            }
        });

        mschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecificChat.this,AlarmActivity.class);
                intent.putExtra("studentid",mreceiveruid);
                startActivity(intent);
            }
        });

        mvideocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setPackage("com.google.android.apps.tachyon");
                intent.setAction("com.google.android.apps.tachyon.action.CALL");
                intent.setData(Uri.parse("tel:"+PhoneNumber1));
                startActivity(intent);
            }
        });

        maccessButtonnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpecificChat.this,AccessCodeActivity.class);
                intent.putExtra("studentid11",mreceiveruid);
                startActivity(intent);
            }
        });

        DatabaseReference databaseReference=firebaseDatabase.getReference().child("chats").child(senderRoom).child("messages");
        messagesAdapter=new MessageAdapter(SpecificChat.this,messagesArrayList);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    messages messages=snapshot1.getValue(messages.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        mbackbuttonofspecificchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mnameofspecificuser.setText(mreceivername);
        msendbuton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enteredmessage = msendmessage.getText().toString();
                if(enteredmessage.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Type a message!", Toast.LENGTH_SHORT).show();
                }
                else

                {
                    Date date=new Date();
                    currenttime=simpleDateFormat.format(calendar.getTime());
                    messages messages=new messages(enteredmessage,firebaseAuth.getUid(),date.getTime(),currenttime);
                    firebaseDatabase=FirebaseDatabase.getInstance("https://anonymous-9454d-default-rtdb.asia-southeast1.firebasedatabase.app");
                    firebaseDatabase.getReference().child("chats")
                            .child(senderRoom)
                            .child("messages")
                            .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference()
                                    .child("chats")
                                    .child(receiverRoom)
                                    .child("messages")
                                    .push()
                                    .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });
                        }
                    });

                    msendmessage.setText(null);


                }
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        messagesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(messagesAdapter!=null)
        {
            messagesAdapter.notifyDataSetChanged();
        }
    }
}