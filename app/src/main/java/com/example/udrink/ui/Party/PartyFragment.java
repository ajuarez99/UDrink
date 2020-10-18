package com.example.udrink.ui.Party;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.udrink.R;

public class PartyFragment extends Fragment {

    private PartyViewModel mViewModel;

    public static PartyFragment newInstance() {
        return new PartyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        System.out.println("sad");
        return inflater.inflate(R.layout.fragment_party, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PartyViewModel.class);
        // TODO: Use the ViewModel
    }

}