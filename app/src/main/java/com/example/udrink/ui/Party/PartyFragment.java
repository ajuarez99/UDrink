package com.example.udrink.ui.Party;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.example.udrink.Firebase.FirebasePartyUtil;
import com.example.udrink.Firebase.FirebaseUsersUtil;
import com.example.udrink.Models.Party;
import com.example.udrink.Models.User;
import com.example.udrink.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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
    private Button create, join, leave;
    private TextView pName;
    private EditText et1, et2;
    private FirestoreRecyclerAdapter adapter;
    private String uid;
    private String pid;
    private User temp;
    private View view;
    private FirebaseUsersUtil fUU;
    private FirebasePartyUtil fPU;
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
                if (!partyName.equals("")) {
                    fPU.getParty(partyName, new FirebasePartyUtil.FireStorePartyCallback() {
                        @Override
                        public void partyFound(DocumentSnapshot party) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Party name taken").setTitle("Sorry");
                            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                        @Override
                        public void partyMissing(String pid) {
                            addParty(pid);
                            setView();
                        }
                    });
                }
            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String partyName = et2.getText().toString();
                et2.getText().clear();
                if (!partyName.equals("")) {
                    fPU.getParty(partyName, new FirebasePartyUtil.FireStorePartyCallback() {
                        @Override
                        public void partyFound(DocumentSnapshot party) {
                            if ((boolean) party.get("activeParty") == true) {
                                party.getReference().update("members", FieldValue.arrayUnion(uid));
                                pid = (String) party.get("partyName");
                                db.collection("users").document(uid).update("partyId", pid);
                                setView();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Party not found").setTitle("Sorry");
                                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }

                        @Override
                        public void partyMissing(String pid) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("Party not found").setTitle("Sorry");
                            builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
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
                    pName.setVisibility(View.GONE);
                }
                else {
                    pid = user.get("partyId").toString();
                    pName.setText(pid);
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

    private void addParty(String pid) {
        Party toAdd = new Party(pid);
        toAdd.addMember(uid);
        db.collection("users").document(uid).update("partyId", pid);
        fPU.addParty(toAdd);
        this.pid = pid;
    }
    private void leaveParty(String pid) {
        fPU.getParty(pid, new FirebasePartyUtil.FireStorePartyCallback() {
            @Override
            public void partyFound(DocumentSnapshot party) {
                DocumentReference userParty = party.getReference();
                userParty.update("members", FieldValue.arrayRemove(uid));
                db.collection("users").document(uid).update("partyId", null);
                ArrayList<String> members = (ArrayList<String>) party.get("members");
                if(members.size() == 1) {
                    userParty.update("activeParty", false);
                }
            }

            @Override
            public void partyMissing(String pid) {

            }
        });
        partyRview.setVisibility(View.GONE);
        leave.setVisibility(View.GONE);
        pName.setVisibility(View.GONE);
        create.setVisibility(View.VISIBLE);
        join.setVisibility(View.VISIBLE);
        et1.setVisibility(View.VISIBLE);
        et2.setVisibility(View.VISIBLE);
        adapter.stopListening();
        this.pid = null;
    }

    private void setView() {

        partyRview.setVisibility(View.VISIBLE);
        pName.setVisibility(View.VISIBLE);
        pName.setText(pid);
        create.setVisibility(View.GONE);
        join.setVisibility(View.GONE);
        et1.setVisibility(View.GONE);
        et2.setVisibility(View.GONE);
        leave.setVisibility(View.VISIBLE);
        setPartyFeed();
        adapter.startListening();
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
            name = itemView.findViewById(R.id.userName);
        }


    }

    public class PartyNameTakenDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Party name taken. Please choose another name.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dismiss();
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }


}
