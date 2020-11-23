package com.example.udrink.ui.Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.udrink.Adapters.ProfileFeedAdapter;
import com.example.udrink.Firebase.FirebaseUsersUtil;
import com.example.udrink.Models.Party;
import com.example.udrink.Models.PartyHistory;
import com.example.udrink.Models.User;
import com.example.udrink.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static android.service.controls.ControlsProviderService.TAG;
import static com.example.udrink.MainActivity.UDRINK_SETTINGS;
import static com.example.udrink.MainActivity.UDRINK_UID;

public class ProfileFragment extends Fragment {

    private static String uid, pid;
    private FirebaseFirestore db;
    private FirebaseStorage storage;
    private TextView heightView;
    private TextView weightView;
    private CircleImageView profilePic;
    private static final int REQUEST_IMAGE = 2;
    FirebaseUsersUtil util;


    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final Context con = this.getContext();

        final View root = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextView nameView = root.findViewById(R.id.nameTextView);
        profilePic = root.findViewById(R.id.pic);
        heightView = root.findViewById(R.id.heightView);
        weightView = root.findViewById(R.id.weightView);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        SharedPreferences settings = getActivity().getSharedPreferences(UDRINK_SETTINGS, MODE_PRIVATE);
        uid = settings.getString(UDRINK_UID, "");

        util = new FirebaseUsersUtil();

        util.findUserById(uid, new FirebaseUsersUtil.FireStoreUserCallback() {
            @Override
            public void newUserCallBack(DocumentSnapshot user) {

            }

            @Override
            public void getUserCallback(DocumentSnapshot user) {
                if(user.get("profilePicture") != null)
                    Glide.with(ProfileFragment.this).load(user.get("profilePicture").toString()).into(profilePic);
                else
                    Glide.with(ProfileFragment.this).load(R.drawable.default_profile)
                            .into(profilePic);
                String name = (String) user.get("name");
                long feet = (long) user.get("feet");
                long inches = (long) user.get("inches");
                long weight = (long) user.get("weight");
                nameView.setText(name);
                heightView.setText(getResources().getString(R.string.height, feet, inches));
                weightView.setText(getResources().getString(R.string.weight, weight));
                Log.d(TAG, "DocumentSnapshot data: " + user.getData());

                if(user.get("pid") != null){
                    pid = user.get("pid").toString();
                }

                db.collection("users").document(uid).collection("partyHistory").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                Log.d(TAG, "onSuccess: here -------");
                                FirestoreRecyclerOptions<PartyHistory> options = new FirestoreRecyclerOptions.Builder<com.example.udrink.Models.PartyHistory>()
                                        .setQuery(queryDocumentSnapshots.getQuery(), PartyHistory.class).build();
                                List<PartyHistory> parties = queryDocumentSnapshots.toObjects(PartyHistory.class);
                                for (int i = 0; i < parties.size(); i++){
                                    Log.d(TAG, "party: " + parties.get(i).getName());
                                    Log.d(TAG, "date: " + parties.get(i).getDate());
                                }

                                if (parties.size() == 0){
                                    TextView partiesLabel = root.findViewById(R.id.partiesLabel);
                                    partiesLabel.setText("No Previous Parties");
                                    partiesLabel.setTextSize(40);
                                    partiesLabel.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                }

                                // Setup RecyclerView
                                final RecyclerView recyclerView = root.findViewById(R.id.partyFeedRecyclerView);
                                recyclerView.setAdapter(new ProfileFeedAdapter((ArrayList<PartyHistory>) parties));
                                recyclerView.setHasFixedSize(true);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(con);
                                recyclerView.setLayoutManager(layoutManager);
                                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
                            }
                        });
            }

            @Override
            public void getUserDrinks(DocumentSnapshot drinks) {

            }
        });


//        profileImage.setImageResource(R.drawable.circle);
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });

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
//                            data.put("weight", weight);
//                            final DocumentReference docRef = db.collection("users").document(uid);
//                            docRef.set(data,SetOptions.merge());
                            DocumentReference docRef = db.collection("users").document(uid);
                            docRef.update("weight", weight);
                            weightView.setText(getResources().getString(R.string.weight, weight));
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
//                          data.put("feet", feet);
//                          data.put("inches", inches);
//                          final DocumentReference docRef = db.collection("users").document(uid);
//                          docRef.set(data,SetOptions.merge());
                            DocumentReference docRef = db.collection("users").document(uid);
                            docRef.update("feet", feet, "inches", inches);
                            heightView.setText(getResources().getString(R.string.height, feet, inches));
                        } catch (Exception e) {
                            Log.d(TAG, "onClick: " + e);
                        }
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_IMAGE && resultCode == Activity.RESULT_OK) {
            if(data != null) {
                final Uri uri = data.getData();
                Log.d(TAG, "Uri: " + uri.toString());
                util.uploadUserProfilePic(uid, uri);
                Glide.with(this).load(uri).into(profilePic);
            }
        }
    }

}