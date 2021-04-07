package com.example.eagleeye.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eagleeye.Model.ADLTask;
import com.example.eagleeye.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UnAcceptedtaskadapter extends FirebaseRecyclerAdapter<ADLTask, UnAcceptedtaskadapter.myviewholder> {

    public UnAcceptedtaskadapter(@NonNull FirebaseRecyclerOptions<ADLTask> options) {
        super(options);
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
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("TaskAccepted").push()
                        .setValue(map);
                FirebaseDatabase.getInstance().getReference().child("TaskNotAccepted")
                        .child(getRef(position).getKey()).removeValue();
            }
        });
    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder{
        TextView title,location,description,fee;
        Button accept;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.titletext);
            location=(TextView)itemView.findViewById(R.id.locationtext);
            description=(TextView)itemView.findViewById(R.id.descriptiontext);
            fee=(TextView)itemView.findViewById(R.id.feetext);
            accept=(Button)itemView.findViewById(R.id.volunnteerPersonalAcceptButton);
        }
    }


}
