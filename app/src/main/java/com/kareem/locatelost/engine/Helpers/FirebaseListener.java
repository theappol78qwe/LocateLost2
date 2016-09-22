/*
 * Copyright (c) $today.year.kareem elsayed aly,no one has the authority to edit,delete,copy any part without my permission
 */

package com.kareem.locatelost.engine.Helpers;

import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kareem.locatelost.Interfaces.FirebaseListeners;


/**
 * Created by kareem on 9/10/2016.
 */

public class FirebaseListener {
    DatabaseReference ref;
    FirebaseListeners listener;

    public FirebaseListener(FirebaseListeners listener) {
        ref = FirebaseDatabase.getInstance().getReference();
        this.listener = listener;
    }

    public DatabaseReference getReference() {
        return ref;
    }

    public void initValueEventListener(final Object data, String path, final int ID) {
        ref.child(path).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.ValueEventListener(data, dataSnapshot, ID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(listener.getMyActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initValueEventListener(final Object data, DatabaseReference ref, final int ID) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.ValueEventListener(data, dataSnapshot, ID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(listener.getMyActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initValueEventListener(final Object data, Query query, final int ID) {
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.ValueEventListener(data, dataSnapshot, ID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(listener.getMyActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initSingleValueEventListener(final Object data, String path, final int ID) {
        ref.child(path).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.SingleValueEventListener(data, dataSnapshot, ID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(listener.getMyActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initSingleValueEventListener(final Object data, DatabaseReference ref, final int ID) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.SingleValueEventListener(data, dataSnapshot, ID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(listener.getMyActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void initSingleValueEventListener(final Object data, Query query, final int ID) {
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.SingleValueEventListener(data, dataSnapshot, ID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(listener.getMyActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
