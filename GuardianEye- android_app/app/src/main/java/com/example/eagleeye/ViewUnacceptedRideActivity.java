package com.example.eagleeye;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eagleeye.Adapter.UnAcceptedrideadapter;
import com.example.eagleeye.Model.Ride;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ViewUnacceptedRideActivity extends AppCompatActivity {
    private RecyclerView recview;
    private UnAcceptedrideadapter adapter;
    private Button mBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recview);

        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Ride> options = new FirebaseRecyclerOptions.Builder<Ride>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("RideNotAccepted"), Ride.class)
                .build();

        adapter=new UnAcceptedrideadapter(options,getApplicationContext());
        recview.setAdapter(adapter);

        mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ViewUnacceptedRideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return;
            }
        });

    }
    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }
    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}