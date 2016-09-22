/*
 * Copyright (c) $today.year.kareem elsayed aly,no one has the authority to edit,delete,copy any part without my permission
 */

package com.kareem.locatelost.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.kareem.locatelost.R;

/**
 * Created by kareem on 9/8/2016.
 */

public class LoadingDialog extends Dialog {
    ImageView imageView;

    public LoadingDialog(Context context) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.loading_dialog);
        imageView = (ImageView) findViewById(R.id.loading_dialog_img);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        imageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_logo));
    }
}
