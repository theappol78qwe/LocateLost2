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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.kareem.locatelost.Dialogs.LoadingDialog;
import com.kareem.locatelost.Interfaces.UserListener;
import com.kareem.locatelost.R;
import com.kareem.locatelost.Interfaces.*;
import com.kareem.locatelost.engine.Helpers.FirebaseUserGrabber;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class Login extends AppCompatActivity implements UserListener {

    public static final String LOG_TAG = "Login";
    public static final int LOGIN_CODE = 1;
    private LoadingDialog dialog;
    private FirebaseUserGrabber userGrabber;
    AutoCompleteTextView mEmailView;
    EditText mPasswordView;

    private boolean registered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.general_login);
        initVariables();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            dialog.show();
            identifedUser();
        }
    }

    public void identifedUser() {
        registered = true;
Intent i = new Intent(Login.this,NewMain.class);
        startActivity(i);
    }

    public void initVariables() {
        dialog = new LoadingDialog(this);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.autocompletetextview_login_email);
        mPasswordView = (EditText) findViewById(R.id.edittext_login_password);
        findViewById(R.id.button_login_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        findViewById(R.id.button_login_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login.this.startActivity(new Intent(Login.this, SignUp.class));
                Login.this.finish();
            }
        });
        userGrabber = new FirebaseUserGrabber(this, 0);
    }

    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError("error_invalid_password");
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
        if (registered) {
            cancel = true;
            Toast.makeText(Login.this, "Please wait... \r\n Image still loading", Toast.LENGTH_LONG).show();
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            dialog.show();
            userGrabber.LogInToFireBase(null, email, password, LOGIN_CODE);
        }
    }


    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    @Override
    public Activity getMyActivity() {
        return this;
    }

    @Override
    public void onSucceed(Object Data, int ID) {
        switch (ID) {
            case LOGIN_CODE:
                identifedUser();
                break;
        }

    }

    @Override
    public void onfailure(Object data, int ID) {
        switch (ID) {
            case LOGIN_CODE:
                dialog.dismiss();
                Toast.makeText(Login.this, ((Exception) data).getMessage(),
                        Toast.LENGTH_SHORT).show();
                break;
        }

    }
}

