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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PartyFeedAdapter extends FirestoreRecyclerAdapter<User, PartyFeedAdapter.ViewHolder> {

    private Date startTime;
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

    public PartyFeedAdapter(@NonNull FirestoreRecyclerOptions<User> options, Date partyTime) {
        super(options);
        this.startTime = partyTime;
    }

    @Override
    protected void onBindViewHolder(@NonNull final PartyFeedAdapter.ViewHolder holder, int position, @NonNull final User model) {
        FirebaseFirestore.getInstance().collection("drinks").whereEqualTo("uid", model.getUid()).whereGreaterThan("drankAt", startTime).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       List<Drink> drinks = queryDocumentSnapshots.toObjects(Drink.class);
                       double BAC = calculateBAC(drinks, model.getWeight(), model.getGender());
                        holder.name.setText(model.getName());
                        holder.bacValue.setText(String.format("%.3f", BAC));
                        if(model.getProfilePicture() != null)
                            Glide.with(holder.itemView).load(model.getProfilePicture())
                                    .into(holder.profilePic);
                        else
                            Glide.with(holder.itemView).load(R.drawable.default_profile)
                                    .into(holder.profilePic);
                    }
                });
//        holder.name.setText(model.getName());
//        if(model.getProfilePicture() != null)
//            Glide.with(holder.itemView).load(model.getProfilePicture())
//                    .into(holder.profilePic);
//        else
//            Glide.with(holder.itemView).load(R.drawable.default_profile)
//                    .into(holder.profilePic);
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

    public double calculateBAC(List<Drink> drinks, int weight, String gender) {
        double alcConstant;
        double totalAlc = 0;
        double BAC;

        if(drinks.isEmpty())
            return 0;
        if(gender.equals("Male"))
            alcConstant = 0.73;
        else
            alcConstant = 0.66;
        for(Drink d : drinks) {
            totalAlc += (d.getABV()/100 * d.getOunces());
        }
        BAC = (totalAlc * 5.14)/(weight * alcConstant) - (0.015 * (System.currentTimeMillis() - startTime.getTime())/3600000);

        return BAC;
    }
}
