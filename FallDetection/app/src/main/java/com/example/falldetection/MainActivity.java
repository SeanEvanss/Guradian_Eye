package com.example.falldetection;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    private TextView displayTextview;
    final protected String TAG= "HULLO";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void trackSteps(){
        Log.i(TAG, "trackSteps: Checkpoint 1");
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        SensorEventListener stepDetector = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                    Log.i(TAG, "trackSteps: Checkpoint 2");
                    double loX = sensorEvent.values[0];
                    double loY = sensorEvent.values[1];
                    double loZ = sensorEvent.values[2];

                    double loAccelerationReader = Math.sqrt(Math.pow(loX, 2)
                            + Math.pow(loY, 2)
                            + Math.pow(loZ, 2));

                    DecimalFormat precision = new DecimalFormat("0.00");
                    double ldAccRound = Double.parseDouble(precision.format(loAccelerationReader));

                    if (ldAccRound > 0.3d && ldAccRound < 0.5d) {
                        Log.i(TAG, "trackSteps: Checkpoint 3");
                        displayTextview.setText("Hi it works");

                        new CountDownTimer(5000,50) {
                            @Override
                            public void onTick(long millisUntilFinished) {

                            }

                            @Override
                            public void onFinish() {
                                displayTextview.setText("Ready for the next fall");
                            }
                        }.start();
                    }

                }
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
        sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: HUllo");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayTextview= findViewById(R.id.display_text);
        trackSteps();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}