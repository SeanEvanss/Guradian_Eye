package com.example.eagleeye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InputEmergencyLocationActivity extends AppCompatActivity {
    private EditText mEmergencyLocation;
    private Button mEmergencyNextBtn;
    private Button mElderlyEmergencyBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_emergency);

        mEmergencyLocation = (EditText)findViewById(R.id.elderlyEmergencyLocation);
        mEmergencyNextBtn = (Button)findViewById(R.id.elderlyEmergencyNextButton);
        mElderlyEmergencyBackBtn=(Button)findViewById(R.id.elderlyEmergencyBackButton);

        mEmergencyNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (InputEmergencyLocationActivity.this, FallDetectorActivity.class);
                intent.putExtra("emergencylocation", mEmergencyLocation.getText().toString());
                startActivity(intent);
                finish();
                return;
            }
        });
        mElderlyEmergencyBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (InputEmergencyLocationActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

    }
}