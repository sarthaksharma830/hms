package com.example.sarthak.hms.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.models.Appointment;

import java.text.SimpleDateFormat;
import java.util.List;

public class AppointmentsRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentsRecyclerViewAdapter.MyViewHolder> {

    private List<Appointment> appointments;
    private Context context;

    public AppointmentsRecyclerViewAdapter(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.appointments_list_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        if (appointments.size() == 0) {
            holder.appointmentLayout.setVisibility(View.GONE);
            holder.noAppointmentLayout.setVisibility(View.VISIBLE);
            holder.emptyView.setVisibility(View.GONE);
        } else if (i != appointments.size()) {
            holder.noAppointmentLayout.setVisibility(View.GONE);
            holder.appointmentLayout.setVisibility(View.VISIBLE);
            holder.emptyView.setVisibility(View.GONE);

            Appointment a = appointments.get(i);

            holder.title.setText(a.getComplaint().getTitle());

            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            holder.fromTime.setText(sdf.format(a.getFromTime()));
            holder.toTime.setText(sdf.format(a.getToTime()));

            holder.category.setText(a.getComplaint().getComplaintCategory().getName());
            int categoryRes = 0;
            switch (a.getComplaint().getComplaintCategory().getCode()) {
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
            holder.icon.setImageDrawable(VectorDrawableCompat.create(context.getResources(), categoryRes, context.getTheme()));
        } else {
            holder.noAppointmentLayout.setVisibility(View.GONE);
            holder.appointmentLayout.setVisibility(View.GONE);
            holder.emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size() + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout appointmentLayout, noAppointmentLayout, emptyView;
        ImageView icon;
        TextView title, category, fromTime, toTime;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentLayout = itemView.findViewById(R.id.appointmentLayout);
            noAppointmentLayout = itemView.findViewById(R.id.noAppointmentLayout);
            emptyView = itemView.findViewById(R.id.empty);

            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            category = itemView.findViewById(R.id.category);
            fromTime = itemView.findViewById(R.id.fromTime);
            toTime = itemView.findViewById(R.id.toTime);
        }
    }
}
