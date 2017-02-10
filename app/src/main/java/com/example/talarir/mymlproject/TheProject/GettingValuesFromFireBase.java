package com.example.talarir.mymlproject.TheProject;

import android.content.Context;
import android.widget.Toast;

import com.example.talarir.mymlproject.InterfaceFolder.GettingValuesFromFireBaseInterface;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;

/**
 * Created by talarir on 09/02/2017.
 */

public class GettingValuesFromFireBase implements GettingValuesFromFireBaseInterface {

    Context context;
    //public ArrayList<LatLng> list;

    public GettingValuesFromFireBase(Context context)
    {
        this.context=context;
        //list=new ArrayList<LatLng>();
    }

    @Override
    public void gettingValues(DatabaseReference mFirebaseDatabase, final ArrayList<LatLng> latLngArrayList, final HeatmapTileProvider mProvider, final TileOverlay mOverlay, final GoogleMap mMap)
    {
            mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Toast.makeText(context,"count : "+dataSnapshot.getChildrenCount(),Toast.LENGTH_SHORT).show();

                    for(DataSnapshot child : dataSnapshot.getChildren())
                    {

                        String mainEmail=child.getKey();
                        Toast.makeText(context,child.getKey(), Toast.LENGTH_SHORT).show();
                        LocationEmailAndUidClass locationEmailAndUidClass=child.getValue(LocationEmailAndUidClass.class);
                        String id=locationEmailAndUidClass.getEmail();
                        Double lng=Double.valueOf(locationEmailAndUidClass.getLongitude());
                        Double lat=Double.valueOf(locationEmailAndUidClass.getLatitude());
                        Toast.makeText(context,"email : "+id+" lat : "+lat+" lng : "+lng+" key : "+mainEmail,Toast.LENGTH_LONG).show();
                        latLngArrayList.add(new LatLng(lat,lng));
                    }
                    changeMap(latLngArrayList,mProvider,mOverlay,mMap);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
    }

    public void changeMap(ArrayList<LatLng> latLngArrayList,HeatmapTileProvider mProvider, TileOverlay mOverlay,GoogleMap mMap)
    {
        if (latLngArrayList.size()>0)
        {
            mProvider = new HeatmapTileProvider.Builder()
                    .data(latLngArrayList)
                    .build();
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }

    }

    public static class LocationEmailAndUidClass {
        String email;
        String latitude;
        String longitude;



        public String getEmail() {
            return email;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLongitude() {
            return longitude;
        }
    }

}
