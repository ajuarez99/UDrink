package com.example.udrink;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.udrink.Firebase.FirebaseUsersUtil;
import com.example.udrink.Models.User;
import com.example.udrink.ui.Login.SignInActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    private static String uid;
    public static final String UDRINK_SETTINGS = "udrink_settings";
    public static final String UDRINK_UID = "uid";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_party, R.id.navigation_map,R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {

        }
        SharedPreferences settings = getSharedPreferences(UDRINK_SETTINGS, MODE_PRIVATE);
        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(UDRINK_UID, mFirebaseUser.getUid());
        editor.commit();

        final FirebaseUsersUtil firebaseUtil = new FirebaseUsersUtil();

        firebaseUtil.findUserById(mFirebaseUser.getUid(),new FirebaseUsersUtil.FireStoreUserCallback(){
            @Override
            public void newUserCallBack(DocumentSnapshot user) {
                startingScreenGetUserInfo();
            }
            @Override
            public void getUserCallback(DocumentSnapshot user) {

            }

            @Override
            public void getUserDrinks(DocumentSnapshot drinks) {

            }
        });


    }

    private void startingScreenGetUserInfo(){
        final FirebaseUsersUtil firebaseUtil = new FirebaseUsersUtil();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add User Info to Calculate BAC:");

        LinearLayout lp =new LinearLayout(MainActivity.this);
        lp.setOrientation(LinearLayout.VERTICAL);

        final EditText inputFeet = new EditText(MainActivity.this);
        inputFeet.setHint("feet");
        inputFeet.setInputType(InputType.TYPE_CLASS_NUMBER );

        final EditText inputInches = new EditText(MainActivity.this);
        inputInches.setHint("inches");
        inputInches.setInputType(InputType.TYPE_CLASS_NUMBER );

        final EditText inputWeight = new EditText(MainActivity.this);
        inputWeight.setHint("weight(lbs)");
        inputWeight.setInputType(InputType.TYPE_CLASS_NUMBER );

        final Spinner inputGender = new Spinner(MainActivity.this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Gender, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputGender.setAdapter(adapter);

        lp.addView(inputFeet);
        lp.addView(inputInches);
        lp.addView(inputWeight);
        lp.addView(inputGender);
        builder.setView(lp);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User user = new User(Integer.parseInt(inputWeight.getText().toString()),Integer.parseInt(inputFeet.getText().toString()),Integer.parseInt(inputInches.getText().toString()), inputGender.getSelectedItem().toString());
                user.setUid(mFirebaseUser.getUid());
                user.setName(mFirebaseUser.getDisplayName());

                firebaseUtil.writeNewUser(user);


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

}
