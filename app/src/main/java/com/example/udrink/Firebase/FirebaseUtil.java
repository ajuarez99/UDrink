package com.example.udrink.Firebase;

import com.example.udrink.Models.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtil {
    private DatabaseReference mDatabase;
    public FirebaseUtil() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void writeNewUser(String userId, String name, String email) {

    }
}
