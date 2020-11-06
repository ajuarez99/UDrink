package com.example.udrink.ui.Profile;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.example.udrink.Firebase.FirebaseUsersUtil;
import com.example.udrink.R;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static android.content.Context.MODE_PRIVATE;
import static android.service.controls.ControlsProviderService.TAG;
import static com.example.udrink.MainActivity.UDRINK_SETTINGS;
import static com.example.udrink.MainActivity.UDRINK_UID;

public class ProfileFragment extends Fragment {

    private static String uid;
    private FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView nameView = root.findViewById(R.id.nameTextView);
        final ImageView profileImage = root.findViewById(R.id.profileImage);

        db = FirebaseFirestore.getInstance();

        SharedPreferences settings = getActivity().getSharedPreferences(UDRINK_SETTINGS, MODE_PRIVATE);
        uid = settings.getString(UDRINK_UID, "");

        FirebaseUsersUtil util = new FirebaseUsersUtil();
        final DocumentReference docRef = db.collection("users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String name = (String) document.get("name");
                        String feet = (String) document.get("feet");
                        String inches = (String) document.get("inches");
                        String weight = (String) document.get("weight");
                        nameView.setText(name);
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

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