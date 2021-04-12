package com.example.eagleeye;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.eagleeye.Model.MyGlobals;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ElderlyMenuActivity extends AppCompatActivity {
    private Button  mElderlyRideBtn,mElderlyTaskBtn, mEmergencyAlert;

    final protected String TAG= "HULLO";
    private String emergencyURL= "emergencyalert?id=1";
    private MyGlobals mGlobals;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elderly_menu);

        mGlobals= new MyGlobals(getApplicationContext(),this);

        mElderlyTaskBtn = (Button)findViewById(R.id.elderlyPersonalBtn);
        mElderlyRideBtn = (Button) findViewById(R.id.elderlyRideBtn);
        mEmergencyAlert = (Button) findViewById(R.id.emergencyButton);

        mElderlyTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), CreateTaskActivity.class);
                startActivity(intent);
            }
        });

        mElderlyRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), CreateRideActivity.class);
                startActivity(intent);
            }
        });
        mEmergencyAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogPrompt();
            }
        });

        trackSteps();
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


    protected void alertDialogPrompt(){
        final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(this).setTitle("Emergency alert").setMessage("Are you ok ? An emergency alert will be triggered after 10 seconds");
        dialog.setPositiveButton("I am fine", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i(TAG, "onClick: Alert dialog confirm works");
            }
        });
        final android.app.AlertDialog alert = dialog.create();
        alert.show();

        // Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (alert.isShowing()) {
                    alert.dismiss();
                }
            }
        };

        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
                mGlobals.postRequest("Emergency alert sent", getString(R.string.api_url)+emergencyURL);

                //postRequest("your message here", url+emergencyContact);

            }
        });
        handler.postDelayed(runnable, 2000);
    }

    protected void trackSteps(){

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    double loX = sensorEvent.values[0];
                    double loY = sensorEvent.values[1];
                    double loZ = sensorEvent.values[2];

                    double loAccelerationReader = Math.sqrt(Math.pow(loX, 2)
                            + Math.pow(loY, 2)
                            + Math.pow(loZ, 2));

                    DecimalFormat precision = new DecimalFormat("0.00");
                    double ldAccRound = Double.parseDouble(precision.format(loAccelerationReader));

                    if (ldAccRound > 0.3d && ldAccRound < 0.5d) {
                        alertDialogPrompt();
                    }
                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }



}