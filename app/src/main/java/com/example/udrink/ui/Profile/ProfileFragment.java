package com.example.udrink.ui.Profile;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.IconCompatParcelizer;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udrink.Adapters.ProfileFeedAdapter;
import com.example.udrink.R;

public class ProfileFragment extends Fragment {

    private ProfileViewModel dashboardViewModel;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView nameView = root.findViewById(R.id.nameTextView);
        final ImageView profileImage = root.findViewById(R.id.profileImage);

        nameView.setText("Joe Strobel");
        profileImage.setImageResource(R.drawable.circle);

        // Setup RecyclerView
        final RecyclerView recyclerView = root.findViewById(R.id.partyFeedRecyclerView);
        recyclerView.setAdapter(new ProfileFeedAdapter());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);


        return root;
    }
}