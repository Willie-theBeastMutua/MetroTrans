package com.example.metrotrans;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter<Myadapter.myviewholder> {
private Context context;
ArrayList<schedule> scheduleview;
    public String UserId;
    public Intent intent;
public Myadapter(Context c, ArrayList<schedule> s){
    context = c;
    scheduleview = s;
    intent=new Intent(context,schedulemap.class);

}

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new myviewholder(LayoutInflater.from(context).inflate(R.layout.cardview, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, int position) {
    holder.from.setText("FROM: "+scheduleview.get(position).getFro());
    holder.to.setText("TO: "+scheduleview.get(position).getTo());
    holder.regveh.setText("Vehicle Reg: "+scheduleview.get(position).getVehregno());
    holder.seats.setText("Seats Available: "+scheduleview.get(position).getSeatsno());
    holder.time.setText("Departure Time: "+scheduleview.get(position).getTimer());

        UserId = scheduleview.get(position).getUserid();
    holder.cards.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            intent.putExtra("uid",UserId);
        context.startActivity(intent);
        }
    });
    }

    @Override
    public int getItemCount() {
        return scheduleview.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
    TextView from;
        TextView to;
        TextView regveh;
        TextView time;
        TextView seats;
        CardView cards;



        public myviewholder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.vehfro);
            to = itemView.findViewById(R.id.vehto);
            regveh = itemView.findViewById(R.id.vehireg);
            time = itemView.findViewById(R.id.vehtime);
            seats = itemView.findViewById(R.id.vehseats);
            cards = itemView.findViewById(R.id.card);
        }
    }
}
