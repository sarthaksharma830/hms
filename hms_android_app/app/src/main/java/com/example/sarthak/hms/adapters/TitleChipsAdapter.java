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

import java.util.List;

public class TitleChipsAdapter extends RecyclerView.Adapter<TitleChipsAdapter.MyViewHolder> {

    private final Context context;
    private List<String> titles;

    public void setTitles(List<String> titles) {
        this.titles = titles;
        notifyDataSetChanged();
    }
    private TitleChipOnClickCallback callback;

    public TitleChipsAdapter(Context context, List<String> titles) {
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
        String title = titles.get(i);
        myViewHolder.title.setText(title);
    }

    @Override
    public int getItemCount() {
        return titles.size();
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
                        callback.onClick(titles.get(getAdapterPosition()));
                    }
                });
            }
        }
    }
}
