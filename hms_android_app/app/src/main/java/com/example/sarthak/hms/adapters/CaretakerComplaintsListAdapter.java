package com.example.sarthak.hms.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.button.MaterialButton;
import android.support.design.card.MaterialCardView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.Utils;
import com.example.sarthak.hms.callbacks.IComplaintItemClickCallback;
import com.example.sarthak.hms.models.Complaint;

import java.text.SimpleDateFormat;
import java.util.List;

public class CaretakerComplaintsListAdapter extends RecyclerView.Adapter<CaretakerComplaintsListAdapter.MyViewHolder> {

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
        notifyDataSetChanged();
    }

    private List<Complaint> complaints;
    private Context context;
    private IComplaintItemClickCallback callback;

    public CaretakerComplaintsListAdapter(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.caretaker_complaints_list_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        final Complaint complaint = complaints.get(i);

        holder.title.setText(complaint.getTitle());
        int categoryRes = 0;
        switch (complaint.getComplaintCategory().getCode()) {
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
        holder.categoryIcon.setImageDrawable(VectorDrawableCompat.create(context.getResources(), categoryRes, context.getTheme()));
        holder.category.setText(complaint.getComplaintCategory().getName());
        holder.room.setText(complaint.getStudent().getHostel().getRoomNumber());
        holder.description.setText(complaint.getDescription());
        holder.date.setText(new SimpleDateFormat("dd/MM/yy").format(complaint.getDateTime()));
        switch (complaint.getComplaintStatus()) {
            case 0:
                holder.status.setText("Pending");
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.red));
                holder.expandButton.setTextColor(ContextCompat.getColor(context, R.color.red));
                break;
            case 1:
                holder.status.setText("Scheduled");
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.orange));
                holder.expandButton.setTextColor(ContextCompat.getColor(context, R.color.orange));
                break;
            case 2:
                holder.status.setText("Resolved");
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.green));
                holder.expandButton.setTextColor(ContextCompat.getColor(context, R.color.green));
                break;
        }
        if (callback != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callback.onClick(complaint);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return complaints.size();
    }

    public void setOnItemClickCallback(IComplaintItemClickCallback callback) {
        this.callback = callback;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        TextView title, category, room, date, description, status;
        MaterialButton expandButton;
        LinearLayout descriptionLayout;
        MaterialCardView card;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.categoryIcon);
            category = itemView.findViewById(R.id.category);
            title = itemView.findViewById(R.id.title);
            room = itemView.findViewById(R.id.room);
            date = itemView.findViewById(R.id.date);
            description = itemView.findViewById(R.id.description);
            expandButton = itemView.findViewById(R.id.expandButton);
            descriptionLayout = itemView.findViewById(R.id.descriptionLayout);
            card = itemView.findViewById(R.id.card);
            status = itemView.findViewById(R.id.status);

            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (descriptionLayout.getVisibility() == View.VISIBLE) {
                        descriptionLayout.setVisibility(View.GONE);
                        expandButton.setText("VIEW DESCRIPTION");
                    } else {
                        descriptionLayout.setVisibility(View.VISIBLE);
                        expandButton.setText("HIDE DESCRIPTION");
                    }
                }
            });
        }
    }
}
