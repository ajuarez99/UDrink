package com.example.udrink.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.udrink.Models.Party;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.service.controls.ControlsProviderService.TAG;

public class FirebasePartyUtil {

    private DatabaseReference mDatabase;
    private String id;

    FirebaseFirestore db;

    public FirebasePartyUtil() {
        db = FirebaseFirestore.getInstance();
    }

    public void addParty(Party party, FireStorePartyCallback callback){

        db.collection("party").add(party)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
    public interface FireStorePartyCallback{

    }
}
