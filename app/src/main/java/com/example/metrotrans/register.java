package com.example.metrotrans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class register extends AppCompatActivity {
    Spinner rolespinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        rolespinner = findViewById(R.id.spinner);
        ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(register.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.roleSpinnerArray));
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rolespinner.setAdapter(roleAdapter);
    }
}
