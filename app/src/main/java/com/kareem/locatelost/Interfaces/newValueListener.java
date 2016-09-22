/*
 * Copyright (c) $today.year.kareem elsayed aly,no one has the authority to edit,delete,copy any part without my permission
 */

package com.kareem.locatelost.Interfaces;

import android.app.Activity;

/**
 * Created by kareem on 9/11/2016.
 */

public interface newValueListener {
    public Activity getMyActivity();
    public void onSucceed(Object Data, int ID);
    public void onfailure(Object data, int ID);
}
