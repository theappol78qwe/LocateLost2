/*
 * Copyright (c) $today.year.kareem elsayed aly,no one has the authority to edit,delete,copy any part without my permission
 */

package com.kareem.locatelost.Interfaces;

import android.app.Activity;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by kareem on 9/10/2016.
 */

public interface FirebaseListeners {


    public void ValueEventListener(Object data, DataSnapshot dataSnapshot, int ID);

    public void SingleValueEventListener(Object data, DataSnapshot dataSnapshot, int ID);
    public Activity getMyActivity();
}
