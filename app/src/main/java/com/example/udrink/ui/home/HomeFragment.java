package com.example.udrink.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udrink.Adapters.DrinkFeedAdapter;
import com.example.udrink.Firebase.FirebaseUsersUtil;
import com.example.udrink.Models.Drink;
import com.example.udrink.R;
import com.example.udrink.Util.SwipeToDelete;
import com.example.udrink.Util.UTime;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;
import static com.example.udrink.MainActivity.UDRINK_SETTINGS;
import static com.example.udrink.MainActivity.UDRINK_UID;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static RecyclerView recyclerView;
    private static RecyclerView.LayoutManager layoutManager;
    private static String uid;
    private static UTime getTimeAgo;
    FirestoreRecyclerAdapter adapter;
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView beerName;
        public TextView beerABV;
        public TextView beerOunces;
        public TextView timeAgo;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            beerName = v.findViewById(R.id.userName);
            beerABV = v.findViewById(R.id.bac_value);
            beerOunces = v.findViewById(R.id.ounces);
            timeAgo = v.findViewById(R.id.timeago);
        }
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences settings = getActivity().getSharedPreferences(UDRINK_SETTINGS, MODE_PRIVATE);

       uid = settings.getString(UDRINK_UID, "");

       getTimeAgo  = new UTime();
        /*
        Set up the recycler view for the home feed on drinks
         */
        recyclerView = root.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


        /*
        set up floating action button and its action so user can add drinks
         */
        FloatingActionButton fab = root.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add a Drink");

                LinearLayout lp =new LinearLayout(getContext());
                lp.setOrientation(LinearLayout.VERTICAL);

                final EditText inputBeer = new EditText(getContext());
                inputBeer.setHint("Name of Alcohol");
                inputBeer.setInputType(InputType.TYPE_CLASS_TEXT );

                final EditText inputABV = new EditText(getContext());
                inputABV.setHint("% of Alcohol");
                inputABV.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                final EditText inputOunces = new EditText(getContext());
                inputOunces.setHint("# of Ounces");
                inputOunces.setInputType(InputType.TYPE_CLASS_NUMBER );

                lp.addView(inputBeer);
                lp.addView(inputABV);
                lp.addView(inputOunces);
                builder.setView(lp);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                        Date date = new Date(System.currentTimeMillis());
                        Drink drink = new Drink(inputBeer.getText().toString(),date,Double.parseDouble(inputABV.getText().toString()), Integer.parseInt(inputOunces.getText().toString()));

                        final FirebaseUsersUtil firebaseUtil = new FirebaseUsersUtil();
                        firebaseUtil.addDrinkToUser(uid, drink);


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
       setDrinkFeed();
        return root;
    }
    private void setDrinkFeed(){
        Query query = FirebaseFirestore.getInstance()
                .collection("drinks").whereEqualTo("uid", uid).orderBy("drankAt", Query.Direction.DESCENDING)
                .limit(100);

        FirestoreRecyclerOptions<Drink> options = new FirestoreRecyclerOptions.Builder<Drink>()
                .setQuery(query, Drink.class).build();

        adapter = new DrinkFeedAdapter(options);

        recyclerView.setAdapter(adapter);
        ItemTouchHelper ith = new ItemTouchHelper(new SwipeToDelete((DrinkFeedAdapter) adapter));
        ith.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}