package com.example.eagleeye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.eagleeye.Model.MyGlobals;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateRideActivity extends AppCompatActivity {

    EditText mLocation, mDestination,mFee;
    Button mSubmitBtn;
    ArrayList<Double> starting_coord, ending_corrd= new ArrayList<Double>();
    String starting_address, destination_address;

    String rideShareURL="rideshare?id=1&details=";
    MyGlobals mGlobals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ride);
        String apiKey = getString(R.string.google_direction_api);
        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        mGlobals= new MyGlobals(getApplicationContext(),this);

        mLocation = findViewById(R.id.elderlyCurrentLocation);
        mDestination = (EditText)findViewById(R.id.elderlyPersonalDestination);
        mSubmitBtn = (Button) findViewById(R.id.elderlyPersonalSubmitButton);
        mFee= findViewById(R.id.elderlyTaskFee);

        starting_coord= getCurrentLocation();
        autofill();

        mLocation.addTextChangedListener(submitBtnWatcher);
        mDestination.addTextChangedListener(submitBtnWatcher);
    }
    public ArrayList getCurrentLocation() {

        List<Address> current_loc_addresses;
        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());
        ArrayList<Double> starting= new ArrayList<Double>();


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("SEAN", "onLocationChanged: onLocation failed");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    0);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            //return;
        }

        LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location!= null) {
            double latitude = location.getLatitude();
            double longitude= location.getLongitude();

            starting.clear();
            starting.add(latitude);
            starting.add(longitude);

            Log.d("SEAN", "onLocationChanged: starting location detected");
            try {
                current_loc_addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String address = current_loc_addresses.get(0).getAddressLine(0);

                Log.d("SEAN", "onLocationChanged: "+ address);
                mLocation.setText(address);
                starting_address= address;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            Toast.makeText(this,"No Location detected. Please check your GPS.",Toast.LENGTH_SHORT).show();

        }
        return starting;
    }

    public void autofill(){
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ID));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                //mDestination.setText(place.getName());
                Log.d("SEAN", "onPlaceSelected: "+ place.getName()+" "+place.getLatLng()+" "+place.getId());
                List<Address> current_destination_addresses;


                Geocoder geocoder;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    current_destination_addresses = geocoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    String address = current_destination_addresses.get(0).getAddressLine(0);


                    //mLocation.setText(address);
                    //mDestination.setText(address);

                    if(mLocation.getText().toString().matches("")){
                        mLocation.setText(address);
                        starting_coord.clear();
                        starting_coord.add(place.getLatLng().latitude);
                        starting_coord.add(place.getLatLng().longitude);
                        starting_address= address;
                    }
                    else if(mDestination.getText().toString().matches("")){
                        mDestination.setText(address);

                        ending_corrd.clear();
                        ending_corrd.add(place.getLatLng().latitude);
                        ending_corrd.add(place.getLatLng().longitude);
                        destination_address= address;
                    }

                    /*
                    float result_fee[]={};
                    Log.d("SEAN", "onPlaceSelected: "+starting_coord.get(0)+" "+starting_coord.get(1));
                    Location.distanceBetween(starting_coord.get(0),starting_coord.get(1),
                            place.getLatLng().latitude,place.getLatLng().longitude,result_fee);
                    mFee.setText(result_fee.toString());
                     */

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onError(@NonNull Status status) {
                mDestination.setText(status.toString());
                Log.d("SEAN", "onError: "+ status.toString());
            }
        });

    }

    private TextWatcher submitBtnWatcher= new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //do nothing
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            //do nothing
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if( (mDestination.getText().toString().length() !=0) && (mLocation.getText().toString().length()!=0)){
                mSubmitBtn.setEnabled(true);
                mSubmitBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //onLocationChanged();

                        try {
                            JSONObject ridershareJSON= new JSONObject();
                            ridershareJSON.put("location", starting_address);
                            ridershareJSON.put("destination", destination_address);

                            mGlobals.postRequest("Emergency alert sent", getString(R.string.api_url)+rideShareURL+ridershareJSON.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        /*
                        Toast.makeText(getApplicationContext(), starting_coord.get(0).toString()+" "+starting_coord.get(1).toString()+" "
                                        +ending_corrd.get(0).toString()+" "+ending_corrd.get(1).toString(),
                                Toast.LENGTH_LONG).show();

                        */
                        Toast.makeText(getApplicationContext(), starting_address+" "+destination_address,Toast.LENGTH_LONG).show();
                    }
                });
            }
            else{
                mSubmitBtn.setEnabled(false);
            }
        }
    };

}