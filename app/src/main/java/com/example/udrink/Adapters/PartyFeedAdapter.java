package com.example.udrink.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udrink.Models.User;
import com.example.udrink.R;

import java.util.ArrayList;

public class PartyFeedAdapter extends RecyclerView.Adapter<PartyFeedAdapter.ViewHolder> {

    private ArrayList<User> partyUsers;
    private Context  mContext;

    public PartyFeedAdapter(ArrayList<User> users, Context context){
        this.partyUsers = users;
        this.mContext = context;
    }

    @NonNull
    @Override
    public PartyFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_party_item, parent, false);
        PartyFeedAdapter.ViewHolder holder = new PartyFeedAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PartyFeedAdapter.ViewHolder holder, int position) {
       holder.name.setText(partyUsers.get(position).getName());
    }

    @Override
    public int getItemCount() {
        if(partyUsers != null)
            return partyUsers.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView bacValue;
        TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bacValue = itemView.findViewById(R.id.bac_value);
            name = itemView.findViewById(R.id.partyName);
        }


    }
}
