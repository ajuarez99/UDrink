package com.example.udrink.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.udrink.Models.Party;
import com.example.udrink.Models.PartyHistory;
import com.example.udrink.Models.User;
import com.example.udrink.R;
import com.example.udrink.Util.UTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFeedAdapter extends RecyclerView.Adapter<ProfileFeedAdapter.ViewHolder>{

    private ArrayList<PartyHistory> mDataset;

    public ProfileFeedAdapter(ArrayList<PartyHistory> data) {
        mDataset = data;
    }

    @NonNull
    @Override
    public ProfileFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recylerview_party_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ProfileFeedAdapter.ViewHolder vh = new ProfileFeedAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.partyName.setText(mDataset.get(position).getName());
        UTime time = new UTime();
        Date date = mDataset.get(position).getDate();
        holder.date.setText(time.getTimeAgo(date));
        Glide.with(holder.itemView).load(R.drawable.default_party)
                .into(holder.partyPic);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView partyName;
        public TextView date;
        public CircleImageView partyPic;

        public ViewHolder(View v) {
            super(v);
            partyName = v.findViewById(R.id.userName);
            date = v.findViewById(R.id.dateView);
            partyPic = v.findViewById(R.id.partyPic);
        }
    }


}
