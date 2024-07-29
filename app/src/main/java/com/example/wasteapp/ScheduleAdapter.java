package com.example.wasteapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Schedule> scheduleList;

    public ScheduleAdapter(Context context, ArrayList<Schedule> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.dateTextView.setText(schedule.getDate());
        holder.timeTextView.setText(schedule.getTime());
        holder.locationTextView.setText(schedule.getLocation());
        holder.wasteTypeTextView.setText(schedule.getWasteType());
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, timeTextView, locationTextView, wasteTypeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.tvDate);
            timeTextView = itemView.findViewById(R.id.tvTime);
            locationTextView = itemView.findViewById(R.id.tvLocation);
            wasteTypeTextView = itemView.findViewById(R.id.tvWasteType);
        }
    }
}
