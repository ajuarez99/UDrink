package com.example.udrink;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.udrink.Firebase.FirebaseUsersUtil;
import com.example.udrink.Firebase.FirebaseUtil;
import com.example.udrink.Models.User;
import com.example.udrink.ui.Login.SignInActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

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
        FirebaseUsersUtil firebaseUtil = new FirebaseUsersUtil();

        User currentUser = new User(mFirebaseUser.getUid(),mFirebaseUser.getDisplayName());
        if(firebaseUtil.findUserById(mFirebaseUser.getUid()) == null)
            firebaseUtil.writeNewUser(currentUser);
    }
    public static void getUid(String Uid){
        uid = Uid;
    }
}
