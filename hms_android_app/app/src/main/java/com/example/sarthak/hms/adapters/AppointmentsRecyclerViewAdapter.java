package com.example.sarthak.hms.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.sarthak.hms.R;

public class AppointmentsRecyclerViewAdapter extends RecyclerView.Adapter<AppointmentsRecyclerViewAdapter.MyViewHolder> {

    private int count = 0;
    private Context context;

    public AppointmentsRecyclerViewAdapter(int count) {
        this.count = count;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.appointments_list_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        if (count == 0) {
            holder.appointmentLayout.setVisibility(View.GONE);
            holder.noAppointmentLayout.setVisibility(View.VISIBLE);
            holder.emptyView.setVisibility(View.GONE);
        } else if (i != count) {
            holder.noAppointmentLayout.setVisibility(View.GONE);
            holder.appointmentLayout.setVisibility(View.VISIBLE);
            holder.emptyView.setVisibility(View.GONE);
        } else {
            holder.noAppointmentLayout.setVisibility(View.GONE);
            holder.appointmentLayout.setVisibility(View.GONE);
            holder.emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return count + 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout appointmentLayout, noAppointmentLayout, emptyView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentLayout = itemView.findViewById(R.id.appointmentLayout);
            noAppointmentLayout = itemView.findViewById(R.id.noAppointmentLayout);
            emptyView = itemView.findViewById(R.id.empty);
        }
    }
}
