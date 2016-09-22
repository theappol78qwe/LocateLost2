/*
 * Copyright (c) $today.year.kareem elsayed aly,no one has the authority to edit,delete,copy any part without my permission
 */

package com.kareem.locatelost.activity.general;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.kareem.locatelost.IntroduceApp;
import com.kareem.locatelost.R;
import com.kareem.locatelost.engine.Helpers.FirebaseUserGrabber;
import com.kareem.locatelost.engine.Helpers.SetValueListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.kareem.locatelost.Interfaces.*;

import static android.Manifest.permission.READ_CONTACTS;

import com.kareem.locatelost.Dialogs.LoadingDialog;

public class SignUp extends AppCompatActivity implements newValueListener, UserListener {
    public static final int NewUserCreationID = 0;
    public static final int NewNameUpdaterID = 1;
    public static final int NewIMGUpdaterID = 2;
    public static final int UserResetID = 3;

    private Bitmap img;
    private boolean done = true;
    private Uri image;
    private LoadingDialog dialog;
    String name;
    private FirebaseUserGrabber userGrabber;
    private SetValueListener setValueListener;

    AutoCompleteTextView mEmailView;
    EditText mPasswordView;
    EditText mconfirmPasswordView;
    EditText nameView;
    ImageView uriviewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_signup);
        init_variables();
    }

    public void init_variables() {
        dialog = new LoadingDialog(this);
        setValueListener = new SetValueListener(this);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.autocompletetextview_signup_email);
        mPasswordView = (EditText) findViewById(R.id.edittext_signup_password);
        mconfirmPasswordView = (EditText) findViewById(R.id.edittext_signup_confirmpassword);
        nameView = (EditText) findViewById(R.id.edittext_signup_name);
        uriviewer = (ImageView) findViewById(R.id.signUp_profilePicture);
        findViewById(R.id.button_signup_signup).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignUp();
            }
        });

        findViewById(R.id.bring_image).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //if select image button is pressed
                done = false;
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType("image/*");
                startActivityForResult(i, 32);
            }
        });

        findViewById(R.id.button_signup_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //if login text is pressed
                finish();
                startActivity(new Intent(SignUp.this, Login.class));
            }
        });
    }

    private void attemptSignUp() {
        //if signup button is pressed
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mconfirmPasswordView.setError(null);
        nameView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        String confirmpassword = mconfirmPasswordView.getText().toString();
        name = nameView.getText().toString();
        boolean cancel = false;
        View focusView = mEmailView;
        //check for all fields have the appropriate inputs
        //starts here
        if (!TextUtils.isEmpty(name) && name.length() < 3) {
            nameView.setError("insert_longer_name");
            focusView = nameView;
            cancel = true;
        }
        if (TextUtils.isEmpty(name)) {
            nameView.setError("insert_your_name");
            focusView = nameView;
            cancel = true;
        }

        if (!password.contentEquals(confirmpassword)) {
            mconfirmPasswordView.setError("error_invalid_confirm_password");
            focusView = mconfirmPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(confirmpassword)) {
            mconfirmPasswordView.setError("reenter_yourpassword");
            focusView = mconfirmPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError("error_invalid_password");
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("enter_valid_password");
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("error_field_required");
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError("error_invalid_email");
            focusView = mEmailView;
            cancel = true;
        }

        //ends here

        //resizing selected image state if done or not
        if (!done) {
            cancel = true;
            Toast.makeText(this, "Wait Please...", Toast.LENGTH_SHORT).show();
        }
        //if any input is not allowed
        if (cancel) {
            focusView.requestFocus();
        } else {
            //all fields accepted as inputs
            dialog.show();
            //create user with name,password
            userGrabber = new FirebaseUserGrabber(this, email, password, NewUserCreationID);
        }
    }

    public void uploadImage() {
        //upload image if it exists
        userGrabber.uploadImage("", img, NewIMGUpdaterID);

    }

    public void uploadName() {
        //to upload name for the created user
        userGrabber.updateName("", name, NewNameUpdaterID);
    }

    public void finalizeSignUp() {
        //finish signUp process
        dialog.dismiss();
        finish();
        //navigate to login activity
        startActivity(new Intent(SignUp.this, IntroduceApp.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //if result is complete then extract url image from the given data
            image = data.getData();
            try {
                //scale the image then pass it to the imageviewer
                img = scaleDown(MediaStore.Images.Media.getBitmap(this.getContentResolver(), image), 500, true);
                uriviewer.setImageBitmap(img);

                Toast.makeText(this, "Image Modification succeeded", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        done = true;
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        if (realImage.getWidth() > maxImageSize && realImage.getHeight() > maxImageSize) {
            float ratio = Math.min(
                    (float) maxImageSize / realImage.getWidth(),
                    (float) maxImageSize / realImage.getHeight());
            int width = Math.round((float) ratio * realImage.getWidth());
            int height = Math.round((float) ratio * realImage.getHeight());

            Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                    height, filter);
            return newBitmap;
        }
        return realImage;
    }

    private boolean isEmailValid(String email) {
        //check if the mail is valid
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //check if password is long enough to be accepted
        return password.length() > 7;
    }

    @Override
    public Activity getMyActivity() {
        //return this activity for the listener
        return this;
    }

    @Override
    public void onfailure(Object data, int ID) {
        //called whenever any called method fails
        switch (ID) {
            case NewUserCreationID:
                //creating new user with password ,email has failed
                //message will appear with the appropriate error
                dialog.dismiss();
                break;
            case NewNameUpdaterID:
                ////updating user name has failed
                dialog.dismiss();
                userGrabber.deleteUser("", UserResetID);
                Toast.makeText(this, "Creation failed", Toast.LENGTH_SHORT).show();
                break;
            case NewIMGUpdaterID:
                //uploading image failed
                finalizeSignUp();
                break;
            case UserResetID:
                //reseting user data has failed
                Toast.makeText(SignUp.this, "Error Cannot be resolved \n please Contact Us", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(SignUp.this, SignUp.class));

        }
    }

    @Override
    public void onSucceed(Object Data, int ID) {
        //called whenever any called method succeed
        switch (ID) {
            case NewUserCreationID:
                //creating new user with password ,email succeeded
                uploadName();
                break;
            case NewNameUpdaterID:
                ////updating user name succeeded
                uploadImage();
                break;
            case NewIMGUpdaterID:
                //finish the signup steps
                finalizeSignUp();
                break;
            case UserResetID:
                //if reseting user is succeeded
                dialog.dismiss();
                Toast.makeText(SignUp.this, "Error Occuered While setting your account", Toast.LENGTH_LONG).show();
                finish();
                startActivity(new Intent(SignUp.this, SignUp.class));
                break;
        }
    }
}


