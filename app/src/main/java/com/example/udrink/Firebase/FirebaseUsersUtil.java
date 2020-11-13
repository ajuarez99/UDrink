package com.example.udrink.Firebase;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.udrink.Models.Drink;
import com.example.udrink.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.service.controls.ControlsProviderService.TAG;

public class FirebaseUsersUtil {
    private DatabaseReference mDatabase;
    private String id;

    FirebaseFirestore db;
    FirebaseStorage st;

    public FirebaseUsersUtil() {
        db = FirebaseFirestore.getInstance();
        st = FirebaseStorage.getInstance();
    }

    public void writeNewUser(User userInfo) {

        db.collection("users").document(userInfo.getUid()).set(userInfo)
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
                        else {
                            fireStoreUserCallback.getUserCallback(documentSnapshot);
                        }
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
        abstract void newUserCallBack(DocumentSnapshot user);
        abstract void getUserCallback(DocumentSnapshot user);
        abstract void getUserDrinks(DocumentSnapshot drinks);
    }

/*
Move these to drinks util class later along with getUserDrinks interface
 */
    public void addDrinkToUser(String uid, Drink drink){
        Drink drinkEnter = new Drink(drink , uid);
        db.collection("drinks").document().set(drinkEnter)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });;
    }
    public void getUserDrinksById(String uid, final FireStoreUserCallback fireStoreUserCallback){
        db.collection("drinks").document(uid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        fireStoreUserCallback.getUserDrinks(documentSnapshot);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

    public void uploadUserProfilePic(final String uid, Uri uri) {
        final StorageReference picture = st.getReference(uid).child(uri.getLastPathSegment());
        picture.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    Log.d("Profile", "Upload Successful");
                    picture.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()) {
                                db.collection("users").document(uid)
                                        .update("profilePicture", task.getResult().toString());
                            }
                        }
                    });
                }

                else
                    Log.d("Profile", "Upload Failed");
            }
        });
    }

}
