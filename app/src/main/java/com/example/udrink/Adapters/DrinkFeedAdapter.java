package com.example.udrink.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udrink.Firebase.FirebaseUsersUtil;
import com.example.udrink.Models.Drink;
import com.example.udrink.R;
import com.example.udrink.Util.UTime;
import com.example.udrink.ui.home.HomeFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class DrinkFeedAdapter extends FirestoreRecyclerAdapter<Drink, DrinkFeedAdapter.ViewHolder> {
    private UTime getTimeAgo;

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
            beerName = v.findViewById(R.id.userName);
            beerABV = v.findViewById(R.id.bac_value);
            beerOunces = v.findViewById(R.id.ounces);
            timeAgo = v.findViewById(R.id.timeago);
        }
    }

    public DrinkFeedAdapter(@NonNull FirestoreRecyclerOptions<Drink> options) {
        super(options);
        getTimeAgo  = new UTime();
    }
    @Override
    protected void onBindViewHolder(@NonNull DrinkFeedAdapter.ViewHolder holder, int position, @NonNull Drink model) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        holder.beerName.setText(model.getDrinkName());
        holder.beerABV.setText(String.valueOf(model.getABV()) + "%");
        holder.beerOunces.setText(String.valueOf(model.getOunces())+ " oz.");
        holder.timeAgo.setText(getTimeAgo.getTimeAgo(model.getDrankAt()));
    }

    @NonNull
    @Override
    public DrinkFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Using a custom layout called R.layout.message for each item, we create a new instance of the viewholder
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.recyclerview_feed_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }


}