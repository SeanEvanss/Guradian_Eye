package com.example.eagleeye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class EmergencyReportActivity extends AppCompatActivity {

    TextView mTaskID,mTaskTime;
    private Button mReportBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_report);

        mReportBackBtn=(Button)findViewById(R.id.emergencyReportBackButton);
        mReportBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (EmergencyReportActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mTaskID = (TextView) findViewById(R.id.emergencyid);
        String rand = UUID.randomUUID().toString();
        mTaskID.setText(rand);
        mTaskTime = (TextView) findViewById(R.id.emergencytime);
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        mTaskTime.setText(currentDateTimeString);

        Map<String,Object> map = new HashMap<>();
        map.put("task id",mTaskID.getText().toString());
        map.put("task time",mTaskTime.getText().toString());
        map.put("location",getIntent().getStringExtra("location"));

        FirebaseDatabase.getInstance().getReference().child("EmergencyCompleted").push()
                .setValue(map);

    }
}