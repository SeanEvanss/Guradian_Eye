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

import com.example.eagleeye.DriveActivity;
import com.example.eagleeye.Model.Ride;
import com.example.eagleeye.R;
import com.example.eagleeye.RideReportActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Accceptedrideadapter extends FirebaseRecyclerAdapter<Ride, Accceptedrideadapter.myviewholder> {

    Context context;



    public Accceptedrideadapter(FirebaseRecyclerOptions<Ride> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull Ride model) {
        holder.location.setText(model.getLocation());
        holder.destination.setText(model.getDestination());
        holder.distance.setText(model.getDistance());
        holder.price.setText(model.getPrice());

        Map<String,Object> map = new HashMap<>();
        map.put("location",holder.location.getText().toString());
        map.put("destination",holder.destination.getText().toString());
        map.put("distance",holder.distance.getText().toString());
        map.put("price",holder.price.getText().toString());

        holder.end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase.getInstance().getReference().child("RideAccepted")
                        .child(getRef(position).getKey()).removeValue();

                Intent intent = new Intent (context, RideReportActivity.class);
                intent.putExtra("location",holder.location.getText().toString());
                intent.putExtra("destination",holder.destination.getText().toString());
                intent.putExtra("distance",holder.distance.getText().toString());
                intent.putExtra("price",holder.price.getText().toString());
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
        TextView location,destination,distance,price;
        Button end;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            location=(TextView)itemView.findViewById(R.id.titletext2);
            destination=(TextView)itemView.findViewById(R.id.locationtext2);
            distance=(TextView)itemView.findViewById(R.id.descriptiontext2);
            price=(TextView)itemView.findViewById(R.id.feetext2);
            end=(Button)itemView.findViewById(R.id.volunnteerPersonalEndButton);
        }
    }


}
