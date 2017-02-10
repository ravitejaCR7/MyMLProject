package com.example.talarir.mymlproject.TheProject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.talarir.mymlproject.InterfaceFolder.GettingValuesFromFireBaseInterface;
import com.example.talarir.mymlproject.MainActivity;
import com.example.talarir.mymlproject.PermissionCheckInterface;
import com.example.talarir.mymlproject.PermissionCheckInterfaceImp;
import com.example.talarir.mymlproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinatorRetrieveActivity extends AppCompatActivity implements OnMapReadyCallback {

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private GettingValuesFromFireBaseInterface gettingValuesFromFireBaseInterfaceObj;
    public static ArrayList<LatLng> latLngArrayList;

    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    private GoogleMap mMap;
    PermissionCheckInterface permissionCheckInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator_retrieve);

        permissionCheckInterface= new PermissionCheckInterfaceImp(this);

        latLngArrayList=new ArrayList<LatLng>();
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        gettingValuesFromFireBaseInterfaceObj = new GettingValuesFromFireBase(this);


        setUpMap();
    }

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if (mMap != null) {
            return;
        }
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            permissionCheckInterface.locationPermissionRequest();
            return;
        }
        mMap.setMyLocationEnabled(true);
        //mMap.setTrafficEnabled(true);
        //mMap.setIndoorEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        gettingValuesFromFireBaseInterfaceObj.gettingValues(mFirebaseDatabase,latLngArrayList,mProvider,mOverlay,mMap);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
    }


}
