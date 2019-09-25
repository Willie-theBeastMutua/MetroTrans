package com.example.metrotrans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class viewschedules extends AppCompatActivity {
    DatabaseReference ref;
    RecyclerView recy;
    ArrayList<schedule> sched;
    public Myadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewschedules);
        ref = FirebaseDatabase.getInstance().getReference().child("Schedule");
        recy = findViewById(R.id.myrecycler);
        recy.setLayoutManager(new LinearLayoutManager(this));
        recy.setHasFixedSize(true);
        sched = new ArrayList<>();

        adapter = new Myadapter(viewschedules.this, sched);

        recy.setAdapter(adapter);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    schedule s = dataSnapshot1.getValue(schedule.class);

                    sched.add(s);
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
