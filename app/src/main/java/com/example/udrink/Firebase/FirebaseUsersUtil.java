package com.example.udrink.Firebase;

import android.net.sip.SipSession;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.udrink.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.service.controls.ControlsProviderService.TAG;

public class FirebaseUsersUtil {
    private DatabaseReference mDatabase;

    FirebaseFirestore db;
    public FirebaseUsersUtil() {
        db  = FirebaseFirestore.getInstance();
    }

    public void writeNewUser(String userId, String name) {
        User user = new User(userId, name);
        db.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void findUserById(String uid, final Listener listener){
         final User userReturn = null;
         mDatabase.orderByChild("uid").equalTo(uid).limitToFirst(1)
                 .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                listener.onUserRetrieved(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    interface Listener {
        void onUserRetrieved(User user);
    }
}
