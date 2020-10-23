package com.example.udrink.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.udrink.Models.Drink;
import com.example.udrink.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DrinkFeedAdapter extends RecyclerView.Adapter<DrinkFeedAdapter.ViewHolder> {

    private List<Drink> mDataset;
    public Context context;
    private int listId;

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView beerName;
        public TextView beerABV;
        public TextView beerOunces;
        public TextView timeAgo;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            beerName = v.findViewById(R.id.drinkName);
            beerABV = v.findViewById(R.id.bac);
            beerOunces = v.findViewById(R.id.ounces);
            timeAgo = v.findViewById(R.id.timeago);
        }
    }

    public DrinkFeedAdapter() {
        mDataset = new ArrayList<>();
        for(int i = 0; i< 10; i++){
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Calendar cal = Calendar.getInstance();
           Drink drink = new Drink("busch",cal.getTime() ,12,12);
           mDataset.add(drink);
        }


    }

    @Override
    public DrinkFeedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.recyclerview_feed_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        DrinkFeedAdapter.ViewHolder vh = new DrinkFeedAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final DrinkFeedAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.beerName.setText(mDataset.get(position).getDrinkName());
        holder.beerABV.setText(Integer.toString(mDataset.get(position).getABV()));
        holder.beerOunces.setText(Integer.toString(mDataset.get(position).getOunces()));
        holder.timeAgo.setText(mDataset.get(position).getDrankAt().toString());


    }
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}