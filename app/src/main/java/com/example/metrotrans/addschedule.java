package com.example.metrotrans;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class addschedule extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
Spinner spinnerFrom;
Spinner spinnerTo;
Spinner spinnernum;
Button clock;
TextView time;
EditText regno;
Button adds;
private FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addschedule);
        regno = findViewById(R.id.textreg);
        adds= findViewById(R.id.button2);
        adds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scheduler();
            }
        });
        mauth = FirebaseAuth.getInstance();

        spinnerFrom = findViewById(R.id.spinnerfrom);
        ArrayAdapter<String> fromAdapter = new ArrayAdapter<>(addschedule.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.fromspinner));
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFrom.setAdapter(fromAdapter);
        spinnerTo = findViewById(R.id.spinnerto);
        ArrayAdapter<String> toAdapter = new ArrayAdapter<>(addschedule.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.fromspinner));
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTo.setAdapter(toAdapter);
        spinnernum = findViewById(R.id.spinnerSeats);
        ArrayAdapter<String> seatAdapter = new ArrayAdapter<>(addschedule.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.numbers));
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnernum.setAdapter(seatAdapter);
        clock= findViewById(R.id.button);
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment clockpick = new timepickerfragment();
                clockpick.show(getSupportFragmentManager(),"time picker");
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    time = findViewById(R.id.textAddtime);
    time.setText(hourOfDay+":"+minute);
    }
    public void scheduler(){
        final String from = spinnerFrom.getSelectedItem().toString();
        final String to = spinnerTo.getSelectedItem().toString();
        final String registration = regno.getText().toString().trim();
        final String seat = spinnernum.getSelectedItem().toString();
        final String times = time.getText().toString();
        final String user = FirebaseAuth.getInstance().getCurrentUser()
                .getUid();
        if(registration.isEmpty()){
            regno.setError("Please enter vehicle registration");
            regno.requestFocus();
            return;
        }
        schedule sched = new schedule(
                from,
                to,
                registration,
                seat,
                times,
                user

        );
        FirebaseDatabase.getInstance().getReference().child("Schedule").push().setValue(sched).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(addschedule.this, "Schedule added", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(addschedule.this, driverlocation.class));
                }
                else{
                    Toast.makeText(addschedule.this, "scheduling failed please try again", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(addschedule.this, addschedule.class));

                }
            }
        });


    }
}
