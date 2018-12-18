package com.google.mbarte.barte_movieworld;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private List<ListSchedule> listSchedules;
    private Context context;

    public ScheduleAdapter(List<ListSchedule>listSchedules, Context context)
    {
        this.listSchedules = listSchedules;
        this.context = context;
    }

    @Override
    public ScheduleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.schedule_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAdapter.ViewHolder holder, int position) {
        ListSchedule listSchedule = listSchedules.get(position);

        holder.textViewHead.setText(listSchedule.getHead());
        holder.textViewDesc.setText(listSchedule.getDesc());
        holder.textViewBottom.setText(listSchedule.getBottoms());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReserveSeatActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listSchedules.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView textViewHead;
        public TextView textViewDesc;
        public TextView textViewBottom;
        public LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewHead = (TextView)itemView.findViewById(R.id.textHead1);
            textViewDesc = (TextView)itemView.findViewById(R.id.textDesc1);
            textViewBottom = (TextView)itemView.findViewById(R.id.textBottom1);


            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout1);
        }
    }
}
