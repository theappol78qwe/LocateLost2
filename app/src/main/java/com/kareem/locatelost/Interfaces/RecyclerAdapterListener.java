/*
 * Copyright (c) $today.year.kareem elsayed aly,no one has the authority to edit,delete,copy any part without my permission
 */

package com.kareem.locatelost.Interfaces;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;

/**
 * Created by kareem on 9/11/2016.
 */

public interface RecyclerAdapterListener<T, E extends RecyclerView.ViewHolder> {
    public void RecyclerAdapterListener(E ViewHolder, T Holder, int position, int ID);
    public Activity getMyActivity();
}
