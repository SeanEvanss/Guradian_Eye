package com.example.eagleeye;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomerMenuActivity extends AppCompatActivity {
    private Button mCustomerRideBtn;
    private Button mCustomerMenuBackBtn;
    private Button ViewAcceptedRideBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);
        mCustomerRideBtn = (Button) findViewById(R.id.customerRideBtn);
        ViewAcceptedRideBtn=(Button)findViewById(R.id.customerViewAcceptRideBtn);
        mCustomerMenuBackBtn=(Button)findViewById(R.id.customerMenuBackButton);
        mCustomerRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (CustomerMenuActivity.this, CreateRideActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        mCustomerMenuBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (CustomerMenuActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
        ViewAcceptedRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (CustomerMenuActivity.this, ViewAcceptedRideActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });
    }
}