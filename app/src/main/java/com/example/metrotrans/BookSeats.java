package com.example.metrotrans;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BookSeats extends AppCompatActivity implements OnSeatSelected {

    private static final int COLUMNS = 5;
    private TextView txtSeatSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookseats);

        txtSeatSelected = (TextView)findViewById(R.id.txt_seat_selected);
        txtSeatSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookSeats.this, destinationpicker.class));
            }
        });

        List<AbstractItem> items = new ArrayList<>();
        for (int i=0; i<30; i++) {

            if (i%COLUMNS==0 || i%COLUMNS==4) {
                items.add(new EdgeItem(String.valueOf(i)));
            } else if (i%COLUMNS==1 || i%COLUMNS==3) {
                items.add(new CenterItem(String.valueOf(i)));
            } else {
                items.add(new EmptyItem(String.valueOf(i)));
            }
        }

        GridLayoutManager manager = new GridLayoutManager(this, COLUMNS);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.lst_items);
        recyclerView.setLayoutManager(manager);

        AirplaneAdapter adapter = new AirplaneAdapter(this, items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSeatSelected(int count) {

        txtSeatSelected.setText("Book "+count+" seats");
    }
}
