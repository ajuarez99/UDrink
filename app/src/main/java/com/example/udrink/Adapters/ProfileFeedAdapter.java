package com.example.udrink.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udrink.Models.Party;
import com.example.udrink.Models.User;
import com.example.udrink.R;
import com.example.udrink.Util.UTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProfileFeedAdapter extends RecyclerView.Adapter<ProfileFeedAdapter.ViewHolder>{

    private ArrayList<Party> mDataset;

    public ProfileFeedAdapter() {
        mDataset = new ArrayList<>();
        ArrayList<User> users = new ArrayList<>();
        for(int j = 0; j < 7; j++){
            Party tempParty = new Party("Party: " + j);
            mDataset.add(tempParty);
        }

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
        holder.partyName.setText(mDataset.get(position).getPartyName());
        UTime time = new UTime();
        Date date = new Date();
        date.setTime(50000);
        holder.date.setText(time.getTimeAgo(date));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView partyName;
        public TextView date;

        public ViewHolder(View v) {
            super(v);
            partyName = v.findViewById(R.id.userName);
            date = v.findViewById(R.id.dateView);
        }
    }


}
