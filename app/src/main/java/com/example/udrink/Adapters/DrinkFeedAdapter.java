package com.example.udrink.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.udrink.Firebase.FirebaseUsersUtil;
import com.example.udrink.Models.Drink;
import com.example.udrink.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DrinkFeedAdapter extends RecyclerView.Adapter<DrinkFeedAdapter.ViewHolder> {

    private List<Drink> mDataset;
    public Context context;
    private int listId;
private ChildEventListener mChildListener;
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
            beerName = v.findViewById(R.id.partyName);
            beerABV = v.findViewById(R.id.bac_value);
            beerOunces = v.findViewById(R.id.ounces);
            timeAgo = v.findViewById(R.id.timeago);
        }
    }

    public DrinkFeedAdapter(String uid) {

        final FirebaseUsersUtil firebaseUtil = new FirebaseUsersUtil();

        firebaseUtil.getUserDrinksById(uid, new FirebaseUsersUtil.FireStoreUserCallback() {
            @Override
            public void newUserCallBack(DocumentSnapshot user) {

            }

            @Override
            public void getUserCallback(DocumentSnapshot user) {

            }

            @Override
           public void getUserDrinks(DocumentSnapshot drinks) {
//                User user = new User(drinks.toObject(User.class));
//
//                mDataset.addAll(user.getDrinks());
//                notifyDataSetChanged();
            }
        });

        mDataset = new ArrayList<>();


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
    public void updateData(ArrayList<Drink> viewModels) {
        mDataset.clear();
        mDataset.addAll(viewModels);
        notifyDataSetChanged();
    }
    public void addItem(int position, Drink m) {
        mDataset.add(position, m);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mDataset.remove(position);
        notifyItemRemoved(position);
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