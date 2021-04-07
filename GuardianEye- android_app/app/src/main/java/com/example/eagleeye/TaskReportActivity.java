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


public class TaskReportActivity extends AppCompatActivity {

    TextView mTaskID,mTaskTime;
    private Button mReportBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        mReportBackBtn=(Button)findViewById(R.id.reportBackButton);
        mReportBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (TaskReportActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

        mTaskID = (TextView) findViewById(R.id.taskid);
        String rand = UUID.randomUUID().toString();
        mTaskID.setText(rand);
        mTaskTime = (TextView) findViewById(R.id.tasktime);
        String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());
        mTaskTime.setText(currentDateTimeString);

        Map<String,Object> map = new HashMap<>();
        map.put("task id",mTaskID.getText().toString());
        map.put("task time",mTaskTime.getText().toString());
        map.put("title",getIntent().getStringExtra("title"));
        map.put("location",getIntent().getStringExtra("location"));
        map.put("description",getIntent().getStringExtra("description"));
        map.put("fee",getIntent().getStringExtra("fee"));

        FirebaseDatabase.getInstance().getReference().child("TaskCompleted").push()
                .setValue(map);

    }
}