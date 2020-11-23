package com.example.udrink.ui.Map;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.udrink.Firebase.FirebasePartyUtil;
import com.example.udrink.Models.Party;
import com.example.udrink.Models.ULocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import com.example.udrink.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.example.udrink.MainActivity.UDRINK_SETTINGS;
import static com.example.udrink.MainActivity.UDRINK_UID;
import static com.example.udrink.ui.Party.PartyFragment.UDRINK_PARTY;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;

    LatLng start;

    private FirebasePartyUtil fpu;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFrag = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
        fpu = new FirebasePartyUtil();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return root;
        }else{
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
            // Write you code here if permission already given.
        }

        return root;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(),R.raw.mapstyle_mine));
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(final Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    start = new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
                    SharedPreferences settings = getActivity().getSharedPreferences(UDRINK_SETTINGS, MODE_PRIVATE);
                    String party = settings.getString(UDRINK_PARTY, "");
                    fpu.updatePartyLocation(location,party,new FirebasePartyUtil.FireStorePartyCallback(){
                        @Override
                        public void partyFound(DocumentSnapshot party) {

                        }

                        @Override
                        public void partyMissing(String pid) {

                        }

                        @Override
                        public void updatePartyLocation(DocumentSnapshot party) {
                            DocumentReference loc = party.getReference();
                            loc.update("latitude", location.getLatitude());
                            loc.update("longitude", location.getLongitude());
                        }

                        @Override
                        public void addMarkers(Task<QuerySnapshot> task) {

                        }
                    });
                    // Logic to handle location object
                }
            }
        });
        // Add a marker in Sydney and move the camera
        fpu.getAllParties(new FirebasePartyUtil.FireStorePartyCallback(){
            @Override
            public void partyFound(DocumentSnapshot party) { }

            @Override
            public void partyMissing(String pid) { }

            @Override
            public void updatePartyLocation(DocumentSnapshot party) {}

            @Override
            public void addMarkers(Task<QuerySnapshot> task) {
                for (DocumentSnapshot document : task.getResult()) {
                    Party party = document.toObject(Party.class);
                     LatLng mark = new LatLng(party.getLatitude(),party.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(mark).title(party.getPartyName()));
                }
            }
        });
    }
}