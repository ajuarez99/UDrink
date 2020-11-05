package com.example.udrink.ui.Party;

import androidx.lifecycle.ViewModelProviders;

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

import com.example.udrink.Adapters.PartyFeedAdapter;
import com.example.udrink.Firebase.FirebasePartyUtil;
import com.example.udrink.Firebase.FirebaseUsersUtil;
import com.example.udrink.Models.Party;
import com.example.udrink.Models.User;
import com.example.udrink.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.udrink.MainActivity.UDRINK_SETTINGS;
import static com.example.udrink.MainActivity.UDRINK_UID;

public class PartyFragment extends Fragment {

    private PartyViewModel mViewModel;
    private Context context;
    private RecyclerView partyRview;
    private Button create, join;
    private EditText et1, et2;
    private FirestoreRecyclerAdapter adapter;
    private String uid;
    private User temp;
    private View view;
    private String partyId;
    private FirebaseUsersUtil fUU;


    public static PartyFragment newInstance() {
        return new PartyFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.party_fragment, container, false);

        SharedPreferences settings = getActivity().getSharedPreferences(UDRINK_SETTINGS, MODE_PRIVATE);
        uid = settings.getString(UDRINK_UID, "");
        fUU = new FirebaseUsersUtil();
        create = view.findViewById(R.id.create);
        join = view.findViewById(R.id.join);
        et1 = view.findViewById(R.id.editText2);
        et2 = view.findViewById(R.id.editText3);
        partyRview = view.findViewById(R.id.user_recycler);
        partyRview.setLayoutManager(new LinearLayoutManager(context));
        setPartyFeed();

        //TODO: Change this to navigate to different fragment
        fUU.findUserById(uid, new FirebaseUsersUtil.FireStoreUserCallback() {
            @Override
            public void newUserCallBack(DocumentSnapshot user) {

            }

            @Override
            public void getUserCallback(DocumentSnapshot user) {
                if(user.get("partyId") == null) {
                    partyRview.setVisibility(View.GONE);
                }
                else {
                    create.setVisibility(View.GONE);
                    join.setVisibility(View.GONE);
                    et1.setVisibility(View.GONE);
                    et2.setVisibility(View.GONE);
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
                .collection("users").whereEqualTo("uid", uid);

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
        if(adapter != null)
            adapter.startListening();
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

    private void createParty(){
        Party newParty = new Party(et1.getText().toString());
        FirebasePartyUtil firebasePartyUtil = new FirebasePartyUtil();

    }
}
