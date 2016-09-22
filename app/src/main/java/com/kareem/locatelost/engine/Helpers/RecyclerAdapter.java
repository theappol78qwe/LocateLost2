/*
 * Copyright (c) $today.year.kareem elsayed aly,no one has the authority to edit,delete,copy any part without my permission
 */

package com.kareem.locatelost.engine.Helpers;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kareem.locatelost.Interfaces.RecyclerAdapterListener;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by kareem on 9/11/2016.
 */

public class RecyclerAdapter<T, E extends ViewHolder> {
    RecyclerAdapterListener listeners;
    DatabaseReference ref;

    public RecyclerAdapter(RecyclerAdapterListener listeners) {
        this.listeners = listeners;
        ref = FirebaseDatabase.getInstance().getReference();
    }

    public DatabaseReference getReference() {
        return ref;
    }

    public void initRecyclerAdapterListener(RecyclerView recyclerView, Class<T> V, Class<E> VH, int Layout, Query q, final int ID) {
        recyclerView.setLayoutManager(new LinearLayoutManager(listeners.getMyActivity()));
        recyclerView.setHasFixedSize(true);
        FirebaseRecyclerAdapter<T, E> adapter = new FirebaseRecyclerAdapter<T, E>(V, Layout, VH, q) {
            @Override
            protected void populateViewHolder(E viewHolder, T model, int position) {
                listeners.RecyclerAdapterListener(viewHolder, model, position, ID);
            }
        };
        recyclerView.setAdapter(adapter);
    }

    public void initRecyclerAdapterListener(RecyclerView recyclerView, Class<T> V, Class<E> VH, int Layout, DatabaseReference reference, final int ID) {
        recyclerView.setLayoutManager(new LinearLayoutManager(listeners.getMyActivity()));
        recyclerView.setHasFixedSize(true);
        FirebaseRecyclerAdapter<T, E> adapter = new FirebaseRecyclerAdapter<T, E>(V, Layout, VH, reference) {
            @Override
            protected void populateViewHolder(E viewHolder, T model, int position) {
                listeners.RecyclerAdapterListener(viewHolder, model, position, ID);
            }
        };
        recyclerView.setAdapter(adapter);
    }

}
