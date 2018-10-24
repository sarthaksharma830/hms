package com.example.sarthak.hms.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sarthak.hms.R;

public class RecentComplaintsListAdapter extends RecyclerView.Adapter<RecentComplaintsListAdapter.MyViewHolder> {

    private Context context;

    public RecentComplaintsListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recent_complaints_list_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        VectorDrawableCompat category = null, star = null;
        switch (i) {
            case 2:
                myViewHolder.titleTextView.setText("Door handle broken");
                myViewHolder.statusTextView.setText("Resolved");
                myViewHolder.datetimeTextView.setText("3:00 PM • 4 October, 2018");
                myViewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
                category = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_saw, context.getTheme());
                myViewHolder.categoryIcon.setImageDrawable(category);
                break;
            case 0:
                myViewHolder.titleTextView.setText("Water tap leaking");
                myViewHolder.statusTextView.setText("Pending");
                myViewHolder.datetimeTextView.setText("9:00 AM • 24 October, 2018");
                myViewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
                category = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_pipe, context.getTheme());
                myViewHolder.categoryIcon.setImageDrawable(category);
                break;
            case 4:
                myViewHolder.titleTextView.setText("AC not working");
                myViewHolder.statusTextView.setText("Resolved");
                myViewHolder.datetimeTextView.setText("11:00 AM • 29 August, 2018");
                myViewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
                category = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_socket, context.getTheme());
                myViewHolder.categoryIcon.setImageDrawable(category);
                break;
            case 1:
                myViewHolder.titleTextView.setText("Washing machine not working");
                myViewHolder.statusTextView.setText("Scheduled");
                myViewHolder.datetimeTextView.setText("7:00 PM • 20 October, 2018");
                myViewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.orange));
                category = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_socket, context.getTheme());
                star = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_star_filled, context.getTheme());
                myViewHolder.categoryIcon.setImageDrawable(category);
                myViewHolder.starIcon.setImageDrawable(star);
                break;
            case 3:
                myViewHolder.titleTextView.setText("Router not working");
                myViewHolder.statusTextView.setText("Resolved");
                myViewHolder.datetimeTextView.setText("12:00 PM • 15 September, 2018");
                myViewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
                category = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_server, context.getTheme());
                star = VectorDrawableCompat.create(context.getResources(), R.drawable.ic_star_filled, context.getTheme());
                myViewHolder.categoryIcon.setImageDrawable(category);
                myViewHolder.starIcon.setImageDrawable(star);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView, statusTextView, datetimeTextView;
        ImageView categoryIcon, starIcon;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            statusTextView = itemView.findViewById(R.id.status);
            datetimeTextView = itemView.findViewById(R.id.datetime);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            starIcon = itemView.findViewById(R.id.star);
        }
    }
}
