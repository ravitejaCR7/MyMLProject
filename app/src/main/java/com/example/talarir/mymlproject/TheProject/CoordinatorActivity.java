package com.example.talarir.mymlproject.TheProject;

import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.talarir.mymlproject.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CoordinatorActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference mDatabase;
    Intent fromMojorActivityIntent;

    public Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        ParcelableMajor parcelableMajor=getIntent().getParcelableExtra("currentLocation");

        if (parcelableMajor!=null)
        {
            Toast.makeText(this,parcelableMajor.latitude+"---"+parcelableMajor.longitude,Toast.LENGTH_SHORT).show();

            DatabaseReference myRef = database.getReference("value");
            myRef.child("latitude").setValue(parcelableMajor.latitude);
            myRef.child("longitude").setValue(parcelableMajor.longitude);
            Toast.makeText(this,"done",Toast.LENGTH_SHORT).show();
        }

    }
}
