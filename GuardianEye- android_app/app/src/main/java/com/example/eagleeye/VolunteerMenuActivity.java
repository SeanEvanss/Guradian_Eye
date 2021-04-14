package com.example.eagleeye;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class VolunteerMenuActivity extends AppCompatActivity {
    private Button mVolunteerPersonalBtn,mVolunteerEmergencyBtn, mVolunteerRideBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_menu);


        mVolunteerPersonalBtn = (Button) findViewById(R.id.volunteerPersonalBtn);
        mVolunteerEmergencyBtn = (Button) findViewById(R.id.volunteerEmergencyBtn);
        mVolunteerRideBtn = (Button) findViewById(R.id.volunteerRideBtn);

        mVolunteerPersonalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (VolunteerMenuActivity.this, ViewAcceptedTaskActivity.class);
                startActivity(intent);
            }
        });

        mVolunteerEmergencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (VolunteerMenuActivity.this, ViewAcceptedEmergencyActivity.class);
                startActivity(intent);
            }
        });
        mVolunteerRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (VolunteerMenuActivity.this, CreateRideActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(false){
            super.onBackPressed();
        }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Logout from account")
                    .setMessage("Are you sure you want to logout ?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }
    }
}