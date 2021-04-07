package com.example.eagleeye;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eagleeye.Adapter.UnAcceptedtaskadapter;
import com.example.eagleeye.Model.ADLTask;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class ViewUnacceptedTaskActivity extends AppCompatActivity {
    private RecyclerView recview;
    private UnAcceptedtaskadapter adapter;
    private Button mVolunteerPersonalActivityBackBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recview);

        recview = (RecyclerView) findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<ADLTask> options = new FirebaseRecyclerOptions.Builder<ADLTask>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("TaskNotAccepted"), ADLTask.class)
                .build();

        adapter=new UnAcceptedtaskadapter(options);
        recview.setAdapter(adapter);
        mVolunteerPersonalActivityBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ViewUnacceptedTaskActivity.this, MainActivity.class);
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