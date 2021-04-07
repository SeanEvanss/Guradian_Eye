package com.example.eagleeye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.text.DecimalFormat;
import java.util.Map;

public class FareEstimateActivity extends AppCompatActivity {
    String location;
    String destination;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    TextView mDistance, mPrice;
    Button mBackBtn,mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_estimate);
        mDistance = (TextView) findViewById(R.id.distance);
        mPrice = (TextView) findViewById(R.id.price);
        mBackBtn = (Button)findViewById(R.id.fareEstimateBackButton);
        mSubmitBtn=(Button)findViewById(R.id.fareEstimateSubmitButton);
        double distance = 0;
        double price = 0;
        location = getIntent().getStringExtra("location");
        destination = getIntent().getStringExtra("destination");
        Geocoder geocoder = new Geocoder(this);
        List<Address>addresses;
        try {
            addresses = geocoder.getFromLocationName(location,1);
            Address location_address = addresses.get(0);
            double location_longitude = location_address.getLongitude();
            double location_latitude = location_address.getLatitude();
            Log.d("longitude",String.valueOf(location_longitude));
            Log.d("latitude",String.valueOf(location_latitude));
            addresses = geocoder.getFromLocationName(destination,1);
            Address destination_address = addresses.get(0);
            double destination_longitude = destination_address.getLongitude();
            double destination_latitude = destination_address.getLatitude();
            Log.d("longitude",String.valueOf(destination_longitude));
            Log.d("latitude",String.valueOf(destination_latitude));
            distance = getDistanceFromLatLonInKm(location_latitude,location_longitude,destination_latitude,destination_longitude);
            Log.d("distance",String.valueOf(df2.format(distance)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mDistance.setText(String.valueOf(df2.format(distance))+"km");
        price = distance*5;
        mPrice.setText('$'+String.valueOf(df2.format(price)));
        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (FareEstimateActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processinsert();
                Intent intent = new Intent (FareEstimateActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

    }
    private double getDistanceFromLatLonInKm(double lat1, double lon1,double lat2,double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }
    private double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }
    private void processinsert(){
        Map<String,Object> map = new HashMap<>();
        map.put("location",location.toString());
        map.put("destination",destination.toString());
        map.put("distance",mDistance.getText().toString());
        map.put("price",mPrice.getText().toString());
        FirebaseDatabase.getInstance().getReference().child("RideNotAccepted").push()
                .setValue(map)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Requested for drivers",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(getApplicationContext(),"Fail to add",Toast.LENGTH_LONG).show();
                    }
                });
    }
}