package com.example.udrink.Firebase;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.udrink.Models.Party;
import com.example.udrink.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.service.controls.ControlsProviderService.TAG;

public class FirebasePartyUtil {

    private DatabaseReference mDatabase;
    private String id;

    FirebaseFirestore db;

    public FirebasePartyUtil() {
        db = FirebaseFirestore.getInstance();
    }

    public void addParty(final Party party, final String pid){

        db.collection("party").document(pid).set(party)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Party written with ID: " + pid);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }
    public void updatePartyLocation(final Location loc ,final String partyName, final FireStorePartyCallback callback){
        db.collection("party").document(partyName).get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                        callback.updatePartyLocation(documentSnapshot);
                }
            });
    }
    public void getAllParties(final FireStorePartyCallback callabck){
        db.collection("party").whereEqualTo("activeParty",true).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            callabck.addMarkers(task);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
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

    public void checkPartyExistence(final String pid, final FireStorePartyCallback callback) {
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
        void updatePartyLocation(DocumentSnapshot party);
        void addMarkers(Task<QuerySnapshot> task);
    }
}
