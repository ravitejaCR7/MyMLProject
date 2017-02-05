package com.example.talarir.mymlproject.InterfaceImplementationFolder;

import android.location.Location;

import com.example.talarir.mymlproject.InterfaceFolder.FireBaseInterface;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by talarir on 02/02/2017.
 */

public class FireBaseInterfaceImp implements FireBaseInterface{

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference myRef = database.getReference("value");



    @Override
    public void connectFire()
    {

    }

    @Override
    public void locationFireValues(Location location)
    {

    }





}
