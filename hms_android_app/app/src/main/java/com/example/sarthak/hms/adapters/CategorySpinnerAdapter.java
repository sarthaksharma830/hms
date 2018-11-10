package com.example.sarthak.hms.adapters;

import android.content.Context;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.models.ComplaintCategory;

import java.util.*;

public class CategorySpinnerAdapter extends ArrayAdapter<ComplaintCategory> {

    private List<ComplaintCategory> categories;
    private LayoutInflater inflater;
    private int resId;
    private Context context;

    public CategorySpinnerAdapter(Context context, int resource, List<ComplaintCategory> categories) {
        super(context, resource, categories);
        this.categories = categories;
        inflater = LayoutInflater.from(context);
        this.resId = resource;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent, R.layout.category_spinner_item_head);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createItemView(position, convertView, parent, R.layout.category_spinner_item);
    }

    private View createItemView(int position, View convertView, ViewGroup parent, int res) {
        final View view = inflater.inflate(res, parent, false);
        TextView categoryTextView = view.findViewById(R.id.category);
        String categoryName = categories.get(position).getName();
        String categoryCode = categories.get(position).getCode();
        categoryTextView.setText(categoryName);
        ImageView categoryImageView = view.findViewById(R.id.categoryIcon);
        int iconRes = 0;
        if (categoryCode.equals("carp")) {
            iconRes = R.drawable.ic_saw;
        } else if (categoryCode.equals("elec")) {
            iconRes = R.drawable.ic_socket;
        } else if (categoryCode.equals("house")) {
            iconRes = R.drawable.ic_clean;
        } else if (categoryCode.equals("tech")) {
            iconRes = R.drawable.ic_server;
        } else if (categoryCode.equals("plumb")) {
            iconRes = R.drawable.ic_pipe;
        }
        VectorDrawableCompat vector = VectorDrawableCompat.create(context.getResources(), iconRes, context.getTheme());
        categoryImageView.setImageDrawable(vector);
        return view;
    }
}
