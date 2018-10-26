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

import java.util.ArrayList;
import java.util.Arrays;

public class CategorySpinnerAdapter extends ArrayAdapter<String> {

    private ArrayList<String> categories;
    private LayoutInflater inflater;
    private int resId;
    private Context context;

    public CategorySpinnerAdapter(Context context, int resource, String[] objects) {
        super(context, resource, objects);

        this.categories = new ArrayList<String>(Arrays.asList(objects));
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
        String category = categories.get(position);
        categoryTextView.setText(category);
        ImageView categoryImageView = view.findViewById(R.id.categoryIcon);
        int iconRes;
        if (category.equals("Carpenter")) {
            iconRes = R.drawable.ic_saw;
        } else if (category.equals("Electrician")) {
            iconRes = R.drawable.ic_socket;
        } else if (category.equals("Housekeeping")) {
            iconRes = R.drawable.ic_clean;
        } else if (category.equals("Technical Support")) {
            iconRes = R.drawable.ic_server;
        } else {
            iconRes = R.drawable.ic_pipe;
        }
        VectorDrawableCompat vector = VectorDrawableCompat.create(context.getResources(), iconRes, context.getTheme());
        categoryImageView.setImageDrawable(vector);
        return view;
    }
}
