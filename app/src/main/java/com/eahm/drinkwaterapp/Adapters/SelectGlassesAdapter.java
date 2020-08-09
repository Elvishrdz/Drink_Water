package com.eahm.drinkwaterapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eahm.drinkwaterapp.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelectGlassesAdapter extends BaseAdapter {

    private Context context;
    String[] defaultAmounts = {
            "100",
            "200",
            "300",
            "400",
            "10",
            "30",
            "50",
            "70"};


    public SelectGlassesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
       return defaultAmounts.length;
    }

    @Override
    public String getItem(int position) {
        return defaultAmounts[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_glass, viewGroup, false);
        }

        TextView textViewGlassAmount =  view.findViewById(R.id.textViewGlassAmount);
        TextView textViewGlassText =  view.findViewById(R.id.textViewGlassText);
        ImageView imageViewGlassIcon = view.findViewById(R.id.imageViewGlassIcon);

        textViewGlassAmount.setText(defaultAmounts[position]);
        textViewGlassText.setText(context.getResources().getString(R.string.text_mililiters));

        return view;
    }

}
