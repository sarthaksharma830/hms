package com.example.sarthak.hms.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.callbacks.TitleChipOnClickCallback;

public class TitleChipsAdapter extends RecyclerView.Adapter<TitleChipsAdapter.MyViewHolder> {

    private final Context context;
    private String[] titles;
    private TitleChipOnClickCallback callback;

    public TitleChipsAdapter(Context context, String[] titles) {
        this.context = context;
        this.titles = titles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.title_chip, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        String title = titles[i];
        myViewHolder.title.setText(title);
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public void setOnTitleChipClickCallback(TitleChipOnClickCallback callback) {
        this.callback = callback;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        Chip title;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            if (callback != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callback.onClick(titles[getAdapterPosition()]);
                    }
                });
            }
        }
    }
}
