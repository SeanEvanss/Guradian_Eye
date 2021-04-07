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

import com.example.eagleeye.Model.ADLTask;
import com.example.eagleeye.R;
import com.example.eagleeye.TaskReportActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Acceptedtaskadapter extends FirebaseRecyclerAdapter<ADLTask, Acceptedtaskadapter.myviewholder> {

    Context context;

    public Acceptedtaskadapter(@NonNull FirebaseRecyclerOptions<ADLTask> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ADLTask model) {
        holder.title.setText(model.getTitle());
        holder.location.setText(model.getLocation());
        holder.description.setText(model.getDescription());
        holder.fee.setText(model.getFee());

        Map<String,Object> map = new HashMap<>();
        map.put("title",holder.title.getText().toString());
        map.put("location",holder.location.getText().toString());
        map.put("description",holder.description.getText().toString());
        map.put("fee",holder.fee.getText().toString());

        holder.end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("TaskAccepted")
                        .child(getRef(position).getKey()).removeValue();

                Intent intent = new Intent (context, TaskReportActivity.class);
                intent.putExtra("title",holder.title.getText().toString());
                intent.putExtra("location",holder.location.getText().toString());
                intent.putExtra("description",holder.description.getText().toString());
                intent.putExtra("fee",holder.fee.getText().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return;
            }
        });
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow2,parent,false);
        return new myviewholder(view);
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView title,location,description,fee;
        Button end;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.titletext2);
            location=(TextView)itemView.findViewById(R.id.locationtext2);
            description=(TextView)itemView.findViewById(R.id.descriptiontext2);
            fee=(TextView)itemView.findViewById(R.id.feetext2);
            end=(Button)itemView.findViewById(R.id.volunnteerPersonalEndButton);
        }
    }


}
