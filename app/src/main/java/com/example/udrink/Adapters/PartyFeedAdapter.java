package com.example.udrink.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.udrink.Models.Drink;
import com.example.udrink.Models.Party;
import com.example.udrink.Models.User;
import com.example.udrink.R;
import com.example.udrink.Util.UTime;
import com.example.udrink.ui.Party.PartyFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PartyFeedAdapter extends FirestoreRecyclerAdapter<User, PartyFeedAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView bacValue;
        TextView name;
        CircleImageView profilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bacValue = itemView.findViewById(R.id.bac_value);
            name = itemView.findViewById(R.id.userName);
            profilePic = itemView.findViewById(R.id.pic);
        }
    }

    public PartyFeedAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PartyFeedAdapter.ViewHolder holder, int position, @NonNull User model) {
        holder.name.setText(model.getName());
        if(model.getProfilePicture() != null)
            Glide.with(holder.itemView).load(model.getProfilePicture())
                    .into(holder.profilePic);
        else
            Glide.with(holder.itemView).load(R.drawable.default_profile)
                    .into(holder.profilePic);
    }

    @NonNull
    @Override
    public PartyFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Using a custom layout called R.layout.message for each item, we create a new instance of the viewholder
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.recyclerview_party_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

}
