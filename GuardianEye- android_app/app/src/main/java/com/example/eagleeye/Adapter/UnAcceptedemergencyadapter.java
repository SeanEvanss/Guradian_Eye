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

public class UnAcceptedemergencyadapter extends FirebaseRecyclerAdapter<ADLTask, UnAcceptedemergencyadapter.myviewholder> {

    public UnAcceptedemergencyadapter(@NonNull FirebaseRecyclerOptions<ADLTask> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull ADLTask model) {
        holder.location.setText(model.getLocation());


        Map<String,Object> map = new HashMap<>();
        map.put("location",holder.location.getText().toString());

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("EmergencyAccepted").push()
                        .setValue(map);
                FirebaseDatabase.getInstance().getReference().child("EmergencyNotAccepted")
                        .child(getRef(position).getKey()).removeValue();
            }
        });
    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow3,parent,false);
        return new myviewholder(view);
    }


    class myviewholder extends RecyclerView.ViewHolder{
        TextView title,location,description,fee;
        Button accept;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            location=(TextView)itemView.findViewById(R.id.locationtext3);
            accept=(Button)itemView.findViewById(R.id.emergencyAcceptButton);
        }
    }


}
