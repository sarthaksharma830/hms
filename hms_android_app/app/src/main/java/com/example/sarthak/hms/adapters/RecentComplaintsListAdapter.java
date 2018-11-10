package com.example.sarthak.hms.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.callbacks.IComplaintItemClickCallback;
import com.example.sarthak.hms.callbacks.IComplaintItemStarClickCallback;
import com.example.sarthak.hms.models.Complaint;

import java.text.SimpleDateFormat;
import java.util.List;

public class RecentComplaintsListAdapter extends RecyclerView.Adapter<RecentComplaintsListAdapter.MyViewHolder> {

    private Context context;
    private IComplaintItemClickCallback onItemClickCallback;
    private IComplaintItemStarClickCallback onItemStarClickCallback;
    private List<Complaint> complaints;


    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
        notifyDataSetChanged();
    }

    public RecentComplaintsListAdapter(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public void setOnItemClickCallback(IComplaintItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recent_complaints_list_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Complaint c = complaints.get(i);
        myViewHolder.titleTextView.setText(c.getTitle());

        switch (c.getComplaintStatus()) {
            case 2:
                myViewHolder.statusTextView.setText("Resolved");
                myViewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.green));
                break;
            case 1:
                myViewHolder.statusTextView.setText("Scheduled");
                myViewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.orange));
                break;
            case 0:
                myViewHolder.statusTextView.setText("Pending");
                myViewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
                break;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String datetime = sdf.format(c.getDateTime());
        sdf = new SimpleDateFormat("MMMM d, YYYY");
        datetime += " • ";
        datetime += sdf.format(c.getDateTime());
        myViewHolder.datetimeTextView.setText(datetime);

        int categoryRes = 0;
        switch (c.getComplaintCategory().getCode()) {
            case "elec":
                categoryRes = R.drawable.ic_socket;
                break;
            case "house":
                categoryRes = R.drawable.ic_clean;
                break;
            case "tech":
                categoryRes = R.drawable.ic_server;
                break;
            case "carp":
                categoryRes = R.drawable.ic_saw;
                break;
            case "plumb":
                categoryRes = R.drawable.ic_pipe;
                break;
        }
        myViewHolder.categoryIcon.setImageDrawable(VectorDrawableCompat.create(context.getResources(), categoryRes, context.getTheme()));
        myViewHolder.starIcon.setImageDrawable(VectorDrawableCompat.create(context.getResources(), c.isStarred() ? R.drawable.ic_star_filled : R.drawable.ic_star_border, context.getTheme()));
    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }

    public void setOnItemStarClickCallback(IComplaintItemStarClickCallback onItemStarClickCallback) {
        this.onItemStarClickCallback = onItemStarClickCallback;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, statusTextView, datetimeTextView;
        ImageView categoryIcon;
        ImageButton starIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title);
            statusTextView = itemView.findViewById(R.id.status);
            datetimeTextView = itemView.findViewById(R.id.datetime);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            starIcon = itemView.findViewById(R.id.star);

            if (onItemClickCallback != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickCallback.onClick(complaints.get(getAdapterPosition()));
                    }
                });
            }
            if (onItemStarClickCallback != null) {
                starIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setClickable(false);
                        onItemStarClickCallback.onItemStarClick(v, complaints.get(getAdapterPosition()));
                    }
                });
            }
        }
    }
}
