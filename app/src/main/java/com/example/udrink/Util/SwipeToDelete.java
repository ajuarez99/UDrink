package com.example.udrink.Util;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.udrink.Adapters.DrinkFeedAdapter;
import com.example.udrink.ui.home.HomeFragment;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

public class SwipeToDelete extends ItemTouchHelper.SimpleCallback {
    private DrinkFeedAdapter adapter;

    /**
     * Constructor for swipe to delete functionality that only reacts to left to right swipe
     */
    public SwipeToDelete(DrinkFeedAdapter a) {
        super(0, ItemTouchHelper.LEFT);
        adapter = a;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        adapter.deleteItem(position);
    }
}
