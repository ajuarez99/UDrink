package com.example.udrink.ui.Party;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.udrink.MainActivity.UDRINK_SETTINGS;
import static com.example.udrink.MainActivity.UDRINK_UID;

public class PartyFragment extends Fragment {

    private PartyViewModel mViewModel;
    private Context context;
    private RecyclerView partyRview;
    private Button create, join, leave;
    private TextView pName;
    private EditText et1, et2;
    private FirestoreRecyclerAdapter adapter;
    private String uid;
    private String pid, partyName;
    private Date startTime;
    private User temp;
    private View view;
    private FirebaseUsersUtil fUU;
    private FirebasePartyUtil fPU;
    private FirebaseFirestore db;
    public static final String UDRINK_PARTY = "partyName";



    public static PartyFragment newInstance() {
        return new PartyFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.party_fragment, container, false);
        context = getContext();
        final SharedPreferences settings = getActivity().getSharedPreferences(UDRINK_SETTINGS, MODE_PRIVATE);
        uid = settings.getString(UDRINK_UID, "");
        db = FirebaseFirestore.getInstance();
        fUU = new FirebaseUsersUtil();
        fPU = new FirebasePartyUtil();
        create = view.findViewById(R.id.create);
        join = view.findViewById(R.id.join);
        leave = view.findViewById(R.id.leave);
        et1 = view.findViewById(R.id.createParty);
        et2 = view.findViewById(R.id.joinParty);
        pName = view.findViewById(R.id.partyName);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String partyName = et1.getText().toString();
                et1.getText().clear();
                //Hide keyboard
                InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et1.getWindowToken(), 0);
                if (!partyName.equals(""))
                    generatePIDandAddParty(partyName);
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pidToJoin = et2.getText().toString().toUpperCase();
                et2.getText().clear();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et2.getWindowToken(), 0);
                if (!pidToJoin.equals("")) {
                    fPU.getParty(pidToJoin, new FirebasePartyUtil.FireStorePartyCallback() {
                        @Override
                        public void partyFound(DocumentSnapshot party) {
                            if ((boolean) party.get("activeParty") == true)
                                joinParty(party);
                            else
                                createAlert("Party not found");
                        }

                        @Override
                        public void updatePartyLocation(DocumentSnapshot party) {

                        }

                        @Override
                        public void addMarkers(Task<QuerySnapshot> task) {

                        }

                        @Override
                        public void partyMissing(String pid) {
                            createAlert("Party not found");
                        }
                    });
                }
            }
        });

        leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveParty(pid);
            }
        });

        partyRview = view.findViewById(R.id.user_recycler);
        partyRview.setLayoutManager(new LinearLayoutManager(context));
        partyRview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        //TODO: Change this to navigate to different fragment
        fUU.findUserById(uid, new FirebaseUsersUtil.FireStoreUserCallback() {
            @Override
            public void newUserCallBack(DocumentSnapshot user) {

            }

            @Override
            public void getUserCallback(DocumentSnapshot user) {
                if(user.get("pid") == null) {
                    setJoinView();
                }
                else {
                    pid = user.get("pid").toString();
                    partyName = user.get("partyName").toString();
                    startTime = ((Timestamp) user.get("drinkStartTime")).toDate();
                    setPartyView();
                }
            }

            @Override
            public void getUserDrinks(DocumentSnapshot drinks) {

            }
        });

        return view;
    }

    private void setPartyFeed(){

            Query query = FirebaseFirestore.getInstance()
                    .collection("users").whereEqualTo("pid", pid);

            FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                    .setQuery(query, User.class).build();

            adapter = new PartyFeedAdapter(options, startTime, uid);
            partyRview.setAdapter(adapter);
    }

    private void addParty(String partyName, String pid) {
        Party toAdd = new Party(partyName);
        toAdd.addMember(uid);
        fPU.addParty(toAdd, pid);
        this.pid = pid;
        this.partyName = partyName;
        this.startTime = toAdd.getStartTime();
        updateUser(pid, partyName, startTime);
        setPartyView();
    }

    private void joinParty(DocumentSnapshot party) {
        DocumentReference partyRef = party.getReference();
        partyRef.update("members", FieldValue.arrayUnion(uid));
        this.pid = partyRef.getId();
        this.partyName = party.get("partyName").toString();
        this.startTime = ((Timestamp) party.get("startTime")).toDate();
        updateUser(pid, partyName, startTime);
        setPartyView();
    }

    private void leaveParty(String partyId) {
        fPU.getParty(partyId, new FirebasePartyUtil.FireStorePartyCallback() {
            @Override
            public void partyFound(DocumentSnapshot party) {
                DocumentReference userParty = party.getReference();
                userParty.update("members", FieldValue.arrayRemove(uid));
                updateUser(null, null, null);
                pid = null;
                partyName = null;
                startTime = null;
                ArrayList<String> members = (ArrayList<String>) party.get("members");
                if(members.size() == 1) {
                    userParty.update("activeParty", false);
                }
            }
            @Override
            public void partyMissing(String pid) {
                //Party should never be null if User is leaving party
            }

            @Override
            public void addMarkers(Task<QuerySnapshot> task) {

            }

            @Override
            public void updatePartyLocation(DocumentSnapshot party) {

            }
        });
        setJoinView();
    }

    private void updateUser(String pid, String partyName, Date startTime) {
        DocumentReference user = db.collection("users").document(uid);
        //Add party to user history when leaving
        if(pid == null) {
            Map<String, Object> data = new HashMap<>();
            data.put("name", this.partyName);
            data.put("date", FieldValue.serverTimestamp());
            CollectionReference history = user.collection("partyHistory");
            history.document(this.pid).set(data);
        }
        user.update("pid", pid);
        user.update("partyName", partyName);
        user.update("drinkStartTime", startTime);


    }

    private void setPartyView() {
        partyRview.setVisibility(View.VISIBLE);
        pName.setVisibility(View.VISIBLE);
        pName.setText(partyName + ": " + pid);
        create.setVisibility(View.GONE);
        join.setVisibility(View.GONE);
        et1.setVisibility(View.GONE);
        et2.setVisibility(View.GONE);
        leave.setVisibility(View.VISIBLE);
        setPartyFeed();
        adapter.startListening();
    }

    private void setJoinView(){
        partyRview.setVisibility(View.GONE);
        leave.setVisibility(View.GONE);
        pName.setVisibility(View.GONE);
        create.setVisibility(View.VISIBLE);
        join.setVisibility(View.VISIBLE);
        et1.setVisibility(View.VISIBLE);
        et2.setVisibility(View.VISIBLE);
        if(adapter != null)
            adapter.stopListening();
    }

    private void createAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message).setTitle("Sorry");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void generatePIDandAddParty(final String partyName) {
        char[] acceptable = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        StringBuilder build = new StringBuilder(4);
        Random random = new Random();
        for(int i = 0; i < 4; i++) {
            char c = acceptable[random.nextInt(acceptable.length)];
            build.append(c);
        }
        String pid = build.toString();
        fPU.checkPartyExistence(pid, new FirebasePartyUtil.FireStorePartyCallback() {
            @Override
            public void partyFound(DocumentSnapshot party) {
                generatePIDandAddParty(partyName);
            }

            @Override
            public void partyMissing(String pid) {
                addParty(partyName, pid);
                SharedPreferences settings = getContext().getSharedPreferences(UDRINK_SETTINGS, MODE_PRIVATE);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(UDRINK_PARTY,pid );
                editor.commit();
            }

            @Override
            public void updatePartyLocation(DocumentSnapshot party) {

            }

            @Override
            public void addMarkers(Task<QuerySnapshot> task) {

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
    }

}
