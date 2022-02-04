package com.example.anonymous;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AlarmActivity extends AppCompatActivity {
    TimePicker timePicker,timepicker1;
    DatePicker datePicker;
    EditText title,description;
    android.widget.Button mschedule;
    int hours, min, day, month, year,hours1;
    int min1;
    String mTitle, mDescr;
    String studentid;

    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        timePicker = findViewById(R.id.timepicker);
        timepicker1 = findViewById(R.id.timepicker1);
        datePicker = findViewById(R.id.datepicker);
        title = findViewById(R.id.titleedit);
        description = findViewById(R.id.desText);
        mschedule = findViewById(R.id.setSchedule);
        studentid = getIntent().getStringExtra("studentid").toString();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mschedule.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                day = datePicker.getDayOfMonth();
                month = datePicker.getMonth();
                year = datePicker.getYear();
                hours = timePicker.getHour();
                min = timePicker.getMinute();
                hours1 = timepicker1.getHour();
                min1 = timepicker1.getMinute();
                mTitle = title.getText().toString();
                mDescr = description.getText().toString();
                DocumentReference documentReference = firebaseFirestore.collection("Schedule").document(studentid);
                Map<String, Object> data = new HashMap<>();
                data.put("Day",day);
                data.put("Month", month);
                data.put("Year", year);
                data.put("HoursStart", hours);
                data.put("MinutesStart",min);
                data.put("HoursEnd", hours1);
                data.put("MinutesEnd",min1);
                data.put("Title",mTitle);
                data.put("Description",mDescr);

                documentReference.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();

                    }
                });
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



    }



}
