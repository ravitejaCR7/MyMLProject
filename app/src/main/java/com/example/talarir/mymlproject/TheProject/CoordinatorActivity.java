package com.example.talarir.mymlproject.TheProject;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.talarir.mymlproject.MainActivity;
import com.example.talarir.mymlproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CoordinatorActivity extends AppCompatActivity {


    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private ParcelableMajor parcelableMajor;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    String userId;

    Intent fromMojorActivityIntent;

    public Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        mAuth = FirebaseAuth.getInstance();

        parcelableMajor=getIntent().getParcelableExtra("currentLocation");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Log.d("CooActivity", "onAuthStateChanged:signed_in:" + user.getUid());
                    if (parcelableMajor!=null)
                    {
                        Toast.makeText(getApplicationContext(),parcelableMajor.latitude+"---"+parcelableMajor.longitude+"---"+user.getUid(),Toast.LENGTH_SHORT).show();
                        fireDataInput(user);
                        Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // User is signed out
                    Log.d("CooActivity", "onAuthStateChanged:signed_out");
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

                }

            }
        };
    }

    public void fireDataInput(FirebaseUser user)
    {
        mFirebaseInstance = FirebaseDatabase.getInstance();


        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        mFirebaseDatabase.child(user.getUid()).child("email").setValue(user.getEmail());

        mFirebaseDatabase.child(user.getUid()).child("LatandLon").child("latitude").setValue(parcelableMajor.latitude);
        mFirebaseDatabase.child(user.getUid()).child("LatandLon").child("longitude").setValue(parcelableMajor.longitude);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onDestroy() {
        FirebaseAuth.getInstance().signOut();
        super.onDestroy();
    }
}
