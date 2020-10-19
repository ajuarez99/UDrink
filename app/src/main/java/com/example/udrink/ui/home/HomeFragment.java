package com.example.udrink.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udrink.Adapters.DrinkFeedAdapter;
import com.example.udrink.Models.Drink;
import com.example.udrink.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static RecyclerView recyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager layoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        /*
        Set up the recycler view for the home feed on drinks
         */
        recyclerView = root.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DrinkFeedAdapter();
        recyclerView.setAdapter(mAdapter);

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

                final EditText inputBAC = new EditText(getContext());
                inputBAC.setHint("Name of ABV");
                inputBAC.setInputType(InputType.TYPE_CLASS_NUMBER );

                final EditText inputOunces = new EditText(getContext());
                inputOunces.setHint("# of Ounces");
                inputOunces.setInputType(InputType.TYPE_CLASS_NUMBER );

                lp.addView(inputBeer);
                lp.addView(inputBAC);
                lp.addView(inputOunces);
                builder.setView(lp);


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
                        Date date = new Date(System.currentTimeMillis());
                        Drink drink = new Drink(inputBeer.toString(),date,Integer.getInteger(inputBAC.toString()), Integer.getInteger(inputOunces.toString()));

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

        return root;
    }
}