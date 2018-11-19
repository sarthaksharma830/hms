package com.example.sarthak.hms.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.sarthak.hms.Constants;
import com.example.sarthak.hms.R;

import java.util.List;

public class ComplaintPicturesUriListAdapter extends RecyclerView.Adapter<ComplaintPicturesUriListAdapter.MyViewHolder> {

    private List<Uri> pictures;
    private Context context;

    public ComplaintPicturesUriListAdapter(List<Uri> pictures) {
        this.pictures = pictures;
    }

    public void setPictures(List<Uri> pictures) {
        this.pictures = pictures;
        notifyDataSetChanged();
    }

    public List<Uri> getPictures() {
        return pictures;
    }

    public void addPictures(List<Uri> pictures) {
        this.pictures.addAll(pictures);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        this.context = viewGroup.getContext();
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.removable_complaint_pictures_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        final int pos = i;
        Glide.with(context)
                .load(pictures.get(i))
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(holder.picture);
        holder.picture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setCancelable(true)
                        .setTitle("Remove")
                        .setMessage("Do you want to remove this picture?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                pictures.remove(pos);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return true;
            }
        });
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
