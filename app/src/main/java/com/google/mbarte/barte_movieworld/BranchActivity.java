package com.google.mbarte.barte_movieworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BranchActivity extends AppCompatActivity {


    private RecyclerView recyclerViewer;
    private RecyclerView.Adapter adapter;

    private List<ListBranch> listBranches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branch);

        recyclerViewer = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerViewer.setHasFixedSize(true);
        recyclerViewer.setLayoutManager(new LinearLayoutManager(this));

        listBranches = new ArrayList<>();
        for(int i = 0 ; i < 10; i++)
        {
            ListBranch listBranch = new ListBranch(
                    "Cebu" + (i + 1),
                    "Available"
            );
            listBranches.add(listBranch);
        }
        adapter = new BranchAdapter(listBranches, this);
        recyclerViewer.setAdapter(adapter);
    }
}
