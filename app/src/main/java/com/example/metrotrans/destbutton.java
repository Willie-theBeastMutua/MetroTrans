package com.example.metrotrans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class destbutton extends AppCompatActivity {
public Button butt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destbutton);
        butt = findViewById(R.id.buttondest);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(destbutton.this, destinationpicker.class));
            }
        });
    }
}
