package com.example.talarir.mymlproject.InterfaceFolder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.firebase.database.DatabaseReference;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;

/**
 * Created by talarir on 09/02/2017.
 */

public interface GettingValuesFromFireBaseInterface {
    //public void gettingValues(DatabaseReference mFirebaseDatabase, HeatmapTileProvider mProvider, TileOverlay mOverlay,GoogleMap mMap);
    public void gettingValues(DatabaseReference mFirebaseDatabase,ArrayList<LatLng> latLngArrayList,HeatmapTileProvider mProvider, TileOverlay mOverlay,GoogleMap mMap);
    //public ArrayList<LatLng> retrievingList();
}
