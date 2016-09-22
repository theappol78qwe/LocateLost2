/*
 * Copyright (c) $today.year.kareem elsayed aly,no one has the authority to edit,delete,copy any part without my permission
 */

package com.kareem.locatelost.engine.Helpers;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kareem.locatelost.Interfaces.*;
/**
 * Created by kareem on 9/11/2016.
 */

public class SetValueListener {
    private newValueListener listener;
    DatabaseReference ref;

    public SetValueListener(newValueListener listener) {
        this.listener = listener;
        ref = FirebaseDatabase.getInstance().getReference();
    }
    public DatabaseReference getReference(){
        return ref;
    }

    public void setNewValue(final Object generalData, final Object data, String path, final int ID) {
        ref.child(path).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onSucceed(generalData, ID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(listener.getMyActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                listener.onfailure(generalData, ID);
            }
        });
    }
}
