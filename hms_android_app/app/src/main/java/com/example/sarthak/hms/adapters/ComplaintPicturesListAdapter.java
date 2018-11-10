package com.example.sarthak.hms.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.models.ComplaintPicture;

import java.util.List;

public class ComplaintPicturesListAdapter extends RecyclerView.Adapter<ComplaintPicturesListAdapter.MyViewHolder> {

    private List<ComplaintPicture> pictures;
    private Context context;

    public ComplaintPicturesListAdapter(List<ComplaintPicture> pictures) {
        this.pictures = pictures;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.complaint_pictures_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView picture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            picture = itemView.findViewById(R.id.picture);
        }
    }
}
