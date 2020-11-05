package com.example.udrink.ui.Party;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;



import com.example.udrink.Firebase.FirebaseUsersUtil;
import com.example.udrink.Models.Party;
import com.example.udrink.Models.User;
import com.example.udrink.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static android.content.Context.MODE_PRIVATE;
import static com.example.udrink.MainActivity.UDRINK_SETTINGS;
import static com.example.udrink.MainActivity.UDRINK_UID;

public class PartyFragment extends Fragment {

    private PartyViewModel mViewModel;
    private Context context;
    private RecyclerView partyRview;
    private Button create, join, leave;
    private EditText et1, et2;
    private FirestoreRecyclerAdapter adapter;
    private String uid;
    private String pid;
    private User temp;
    private View view;
    private FirebaseUsersUtil fUU;
    private FirebaseFirestore db;



    public static PartyFragment newInstance() {
        return new PartyFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.party_fragment, container, false);

        final SharedPreferences settings = getActivity().getSharedPreferences(UDRINK_SETTINGS, MODE_PRIVATE);
        uid = settings.getString(UDRINK_UID, "");
        //pid = settings.getString("pid", "");
        db = FirebaseFirestore.getInstance();
        fUU = new FirebaseUsersUtil();
        create = view.findViewById(R.id.create);
        join = view.findViewById(R.id.join);
        leave = view.findViewById(R.id.leave);
        et1 = view.findViewById(R.id.createParty);
        et2 = view.findViewById(R.id.joinParty);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String partyName = et1.getText().toString();
                et1.getText().clear();
                db.collection("users").document(uid).update("partyId", partyName);
                //settings.edit().putString("pid", partyName).commit();
                pid = partyName;
                partyRview.setVisibility(View.VISIBLE);
                create.setVisibility(View.GONE);
                join.setVisibility(View.GONE);
                et1.setVisibility(View.GONE);
                et2.setVisibility(View.GONE);
                leave.setVisibility(View.VISIBLE);
                setPartyFeed();
                adapter.startListening();

            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String partyName = et2.getText().toString();
                et2.getText().clear();
                db.collection("users").document(uid).update("partyId", partyName);
                //settings.edit().putString("pid", partyName).commit();
                pid = partyName;
                partyRview.setVisibility(View.VISIBLE);
                create.setVisibility(View.GONE);
                join.setVisibility(View.GONE);
                et1.setVisibility(View.GONE);
                et2.setVisibility(View.GONE);
                leave.setVisibility(View.VISIBLE);
                setPartyFeed();
                adapter.startListening();
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("users").document(uid).update("partyId", null);
                partyRview.setVisibility(View.GONE);
                leave.setVisibility(View.GONE);
                create.setVisibility(View.VISIBLE);
                join.setVisibility(View.VISIBLE);
                et1.setVisibility(View.VISIBLE);
                et2.setVisibility(View.VISIBLE);
                adapter.stopListening();
                pid = "";
            }
        });
        partyRview = view.findViewById(R.id.user_recycler);
        partyRview.setLayoutManager(new LinearLayoutManager(context));

        //TODO: Change this to navigate to different fragment
        fUU.findUserById(uid, new FirebaseUsersUtil.FireStoreUserCallback() {
            @Override
            public void newUserCallBack(DocumentSnapshot user) {

            }

            @Override
            public void getUserCallback(DocumentSnapshot user) {
                if(user.get("partyId") == null) {
                    partyRview.setVisibility(View.GONE);
                    leave.setVisibility(View.GONE);
                }
                else {
                    pid = user.get("partyId").toString();
                    create.setVisibility(View.GONE);
                    join.setVisibility(View.GONE);
                    et1.setVisibility(View.GONE);
                    et2.setVisibility(View.GONE);
                    setPartyFeed();
                    adapter.startListening();
                }
            }

            @Override
            public void getUserDrinks(DocumentSnapshot drinks) {

            }
        });

        return view;
    }

    private void setPartyFeed(){

        //TODO: Change this to filter based on partyId
            Query query = FirebaseFirestore.getInstance()
                    .collection("users").whereEqualTo("partyId", pid);

            FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                    .setQuery(query, User.class).build();

            adapter = new FirestoreRecyclerAdapter<User, ViewHolder>(options) {
                @Override
                public void onBindViewHolder(ViewHolder holder, int position, User model) {
                    // - get element from your dataset at this position
                    // - replace the contents of the view with that element
                    holder.name.setText(model.getName());
                }

                @Override
                public ViewHolder onCreateViewHolder(ViewGroup group, int i) {
                    // Using a custom layout called R.layout.message for each item, we create a new instance of the viewholder
                    LayoutInflater inflater = LayoutInflater.from(
                            group.getContext());
                    View v = inflater.inflate(R.layout.recyclerview_party_item, group, false);
                    // set the view's size, margins, paddings and layout parameters
                    ViewHolder vh = new ViewHolder(v);
                    return vh;
                }
            };

            partyRview.setAdapter(adapter);
    }


    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
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
