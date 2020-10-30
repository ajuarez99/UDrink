package com.example.udrink.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.udrink.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.service.controls.ControlsProviderService.TAG;

public class FirebaseUsersUtil {
    private DatabaseReference mDatabase;
    private String id;

    FirebaseFirestore db;

    public FirebaseUsersUtil() {
        db = FirebaseFirestore.getInstance();
    }

    public void writeNewUser(String name , String uid) {
        User user = new User(uid,name);
        db.collection("users").document(user.getUid()).set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void findUserById(String uid, final FireStoreUserCallback fireStoreUserCallback) {
        final User userReturn = new User();
        db.collection("users").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.toObject(User.class) == null){
                            fireStoreUserCallback.newUserCallBack(documentSnapshot);
                        }
                        userReturn.copyUser(documentSnapshot.toObject(User.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    public interface FireStoreUserCallback{
        void newUserCallBack(DocumentSnapshot user);
    }
}
