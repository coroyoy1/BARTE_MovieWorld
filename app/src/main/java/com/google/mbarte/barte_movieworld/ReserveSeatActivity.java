package com.google.mbarte.barte_movieworld;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReserveSeatActivity extends AppCompatActivity {

    Button reserveNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_seat);

        reserveNow = (Button)findViewById(R.id.reserveButton);

        reserveNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReserveSeatActivity.this, HomeTabActivity.class);
                startActivity(intent);
            }
        });

    }
}
