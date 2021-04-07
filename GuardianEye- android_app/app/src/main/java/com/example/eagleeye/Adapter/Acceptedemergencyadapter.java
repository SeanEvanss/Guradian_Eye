package com.example.eagleeye.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eagleeye.EmergencyReportActivity;
import com.example.eagleeye.Model.ADLTask;
import com.example.eagleeye.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Acceptedemergencyadapter extends FirebaseRecyclerAdapter<ADLTask, Acceptedemergencyadapter.myviewholder> {

    Context context;

    public Acceptedemergencyadapter(@NonNull FirebaseRecyclerOptions<ADLTask> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ADLTask model) {
        holder.location.setText(model.getLocation());


        Map<String,Object> map = new HashMap<>();
        map.put("location",holder.location.getText().toString());

        holder.end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("EmergencyAccepted")
                        .child(getRef(position).getKey()).removeValue();

                Intent intent = new Intent (context, EmergencyReportActivity.class);
                intent.putExtra("location",holder.location.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return;
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow4,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView title,location,description,fee;
        Button end;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            location=(TextView)itemView.findViewById(R.id.locationtext4);
            end=(Button)itemView.findViewById(R.id.emergencyEndButton);
        }
    }


}
