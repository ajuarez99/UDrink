package com.example.udrink.ui.Profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

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
        final Context con = this.getContext();

        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView nameView = root.findViewById(R.id.nameTextView);
        final ImageView profileImage = root.findViewById(R.id.profileImage);
        final TextView heightView = root.findViewById(R.id.heightView);
        final TextView weightView = root.findViewById(R.id.weightView);

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
                        long feet = (long) document.get("feet");
                        long inches = (long) document.get("inches");
                        long weight = (long) document.get("weight");
                        nameView.setText(name);
                        heightView.setText(getResources().getString(R.string.height, feet, inches));
                        weightView.setText(getResources().getString(R.string.weight, weight));
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                weightView.setText(getResources().getString(R.string.weight, (long) value.get("weight")));
            }
        });

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                heightView.setText(getResources().getString(R.string.height, (long) value.get("feet"), (long) value.get("inches")));
            }
        });

        profileImage.setImageResource(R.drawable.circle);

        heightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeHeightDialog(con);
            }
        });

        weightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeWeightDialog(con);
            }
        });


        // Setup RecyclerView
        final RecyclerView recyclerView = root.findViewById(R.id.partyFeedRecyclerView);
        recyclerView.setAdapter(new ProfileFeedAdapter());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);


        return root;
    }

    private void showChangeWeightDialog(Context c) {
        final EditText weightText = new EditText(c);
        weightText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Edit Weight")
                .setView(weightText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update one field, creating the document if it does not already exist.
                        Map<String, Object> data = new HashMap<>();
                        long weight;
                        try {
                            weight = Long.parseLong(weightText.getText().toString());
                            data.put("weight", weight);
                            final DocumentReference docRef = db.collection("users").document(uid);
                            docRef.set(data,SetOptions.merge());
                        } catch (Exception e) {
                            Log.d(TAG, "onClick: " + e);
                        }
                    }
                })
                .create();
        dialog.show();
    }

    private void showChangeHeightDialog(Context c) {
        final EditText feetText = new EditText(c);
        final EditText inchesText = new EditText(c);
        feetText.setInputType(InputType.TYPE_CLASS_NUMBER);
        feetText.setHint("Feet");
        inchesText.setInputType(InputType.TYPE_CLASS_NUMBER);
        inchesText.setHint("Inches");
        LinearLayout layout = new LinearLayout(c);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(feetText);
        layout.addView(inchesText);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Edit Height")
                .setView(layout)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Update one field, creating the document if it does not already exist.
                        Map<String, Object> data = new HashMap<>();
                        long feet, inches;
                        try {
                            feet = Long.parseLong(feetText.getText().toString());
                            inches = Long.parseLong(inchesText.getText().toString());
                            data.put("feet", feet);
                            data.put("inches", inches);
                            final DocumentReference docRef = db.collection("users").document(uid);
                            docRef.set(data,SetOptions.merge());
                        } catch (Exception e){
                            Log.d(TAG, "onClick: " + e);
                        }
                    }
                })
                .create();
        dialog.show();
    }
}