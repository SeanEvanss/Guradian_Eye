package com.example.eagleeye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DriverMenuActivity extends AppCompatActivity {
    private Button mDriverEmergencyBtn, mDriverRideBtn;
    private Button ViewAcceptedRideBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_menu);
        mDriverEmergencyBtn = (Button) findViewById(R.id.driverEmergencyBtn);
        ViewAcceptedRideBtn=(Button)findViewById(R.id.driverViewAcceptedRideBtn);
        mDriverRideBtn = (Button) findViewById(R.id.driverRideBtn);


        mDriverEmergencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (DriverMenuActivity.this, ViewUnacceptedEmergencyActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        mDriverRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (DriverMenuActivity.this, ViewUnacceptedRideActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

    }
}