package com.example.eagleeye;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RideReportActivity extends AppCompatActivity {

    TextView mTaskID,mTaskTime;
    private Button mReportBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_report);
        mReportBackBtn=(Button)findViewById(R.id.rideReportBackButton);
        mReportBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (RideReportActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mTaskID = (TextView) findViewById(R.id.ridetaskid);
        String rand = UUID.randomUUID().toString();
        mTaskID.setText(rand);
        mTaskTime = (TextView) findViewById(R.id.ridetasktime);
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        mTaskTime.setText(currentDateTimeString);

        Map<String,Object> map = new HashMap<>();
        map.put("task id",mTaskID.getText().toString());
        map.put("task time",mTaskTime.getText().toString());
        map.put("location",getIntent().getStringExtra("location"));
        map.put("destination",getIntent().getStringExtra("destination"));
        map.put("distance",getIntent().getStringExtra("distance"));
        map.put("price",getIntent().getStringExtra("price"));
        FirebaseDatabase.getInstance().getReference().child("RideCompleted").push()
                .setValue(map);

    }
}