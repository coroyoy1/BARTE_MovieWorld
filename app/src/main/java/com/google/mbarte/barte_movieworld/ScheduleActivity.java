package com.google.mbarte.barte_movieworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private RecyclerView recyclerViewer;
    private RecyclerView.Adapter adapter;

    private List<ListSchedule> listSchedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        recyclerViewer = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerViewer.setHasFixedSize(true);
        recyclerViewer.setLayoutManager(new LinearLayoutManager(this));

        listSchedules = new ArrayList<>();
        for(int i = 0; i < 10; i++)
        {
            ListSchedule listSchedule = new ListSchedule(
                    "Cinema 1 - 2D",
                    "December 1, 2018 - (Reserved Seating)",
                    "1:00 PM - 3:30 PM"
            );
            listSchedules.add(listSchedule);
        }
        adapter = new ScheduleAdapter(listSchedules, this);
        recyclerViewer.setAdapter(adapter);

    }
}
