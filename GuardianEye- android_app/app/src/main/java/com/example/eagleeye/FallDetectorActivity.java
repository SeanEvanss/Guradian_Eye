package com.example.eagleeye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class FallDetectorActivity extends AppCompatActivity implements SensorEventListener{
    private SensorManager sensorManager;
    Sensor accelerometer;
    private TextView mFallDetectorText;
    private static final String TAG = "FallDetectorActivity";
    private Button mFallDetectorBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fall_detector);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(FallDetectorActivity.this, accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        mFallDetectorText=(TextView)findViewById(R.id.falldetectortext);
        mFallDetectorBackBtn=(Button)findViewById(R.id.elderlyFallDetectorBackButton);
        mFallDetectorBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (FallDetectorActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            double loX = event.values[0];
            double loY = event.values[1];
            double loZ = event.values[2];


            double loAccelerationReader = Math.sqrt(Math.pow(loX, 2)
                    + Math.pow(loY, 2)
                    + Math.pow(loZ, 2));

            DecimalFormat precision = new DecimalFormat("0.00");
            double ldAccRound = Double.parseDouble(precision.format(loAccelerationReader));
            //Log.d(TAG,String.valueOf(ldAccRound));
            if (ldAccRound > 30) {
                Log.d(TAG,"Fall detected");
                sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY));
                mFallDetectorText.setText("Fall detected. Help is on the way");
                processinsert();
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void processinsert() {
        Map<String, Object> map = new HashMap<>();
        map.put("location", getIntent().getStringExtra("emergencylocation"));
        FirebaseDatabase.getInstance().getReference().child("EmergencyNotAccepted").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Calling for help", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Fail to add", Toast.LENGTH_LONG).show();
                    }
                });
    }
}