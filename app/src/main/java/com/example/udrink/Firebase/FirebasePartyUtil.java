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

    public void addParty(final Party party){

        db.collection("party").document(party.getPartyName()).set(party)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Party written with ID: " + party.getPartyName());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }

    public void getParty(final String pid, final FireStorePartyCallback callback) {
        db.collection("party").document(pid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        callback.partyFound(doc);
                    }
                    else {
                        Log.d(TAG, "No such document");
                        callback.partyMissing(pid);
                    }
                }
                else
                    Log.d(TAG, "Party get failed with ", task.getException());
            }
        });
    }

    public interface FireStorePartyCallback{
        abstract void partyFound(DocumentSnapshot party);
        abstract void partyMissing(String pid);
    }
}
