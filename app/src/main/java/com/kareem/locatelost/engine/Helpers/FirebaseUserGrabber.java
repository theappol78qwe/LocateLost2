/*
 * Copyright (c) $today.year.kareem elsayed aly,no one has the authority to edit,delete,copy any part without my permission
 */

package com.kareem.locatelost.engine.Helpers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kareem.locatelost.activity.general.Login;
import com.kareem.locatelost.Interfaces.UserListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.kareem.locatelost.Interfaces.*;
import com.kareem.locatelost.engine.modules.User;
import com.squareup.picasso.Picasso;

/**
 * Created by kareem on 9/11/2016.
 */

public class FirebaseUserGrabber implements newValueListener {
    private FirebaseAuth auth;
    private FirebaseUser Firebaseuser;
    private FirebaseListener firebaseListener;
    private Activity activity;
    private UserListener listener;
    private User user;
    SetValueListener valueListener;

    public User getUser() {
        return user;
    }

    public FirebaseUserGrabber(UserListener listener) {
        activity = listener.getMyActivity();
        auth = FirebaseAuth.getInstance();
        Firebaseuser = auth.getCurrentUser();
        this.listener = listener;
        valueListener = new SetValueListener(this);
        if (Firebaseuser == null) {
            activity.startActivity(new Intent(activity, Login.class));
            activity.finish();
        } else {
            user = createUser();
        }
    }

    public FirebaseUserGrabber(UserListener userListener, int Fake) {
        activity = userListener.getMyActivity();
        auth = FirebaseAuth.getInstance();
        this.listener = userListener;
        valueListener = new SetValueListener(this);
        if (auth.getCurrentUser() != null) {
            Firebaseuser = auth.getCurrentUser();
            user = createUser();
        }
    }

    public void LogInToFireBase(final Object Data, String email, String password, final int ID) {
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Firebaseuser = auth.getCurrentUser();
                user = createUser();
                listener.onSucceed(Data, ID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onfailure(e, ID);
            }
        });
    }

    public FirebaseUserGrabber(final UserListener listener, final String email, String
            password, final int ID) {
        this.listener = listener;
        this.activity = listener.getMyActivity();
        valueListener = new SetValueListener(this);
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Firebaseuser = authResult.getUser();
                        user = createUser();
                        createUserOnDatabase(ID);
                        listener.onSucceed("", ID);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                listener.onfailure(null, ID);
            }
        });
    }

    public void createUserOnDatabase(int ID) {
        valueListener.setNewValue(null, user, "users/" + getUID(), ID);
    }

    public void changeNameOnDatabase(String name, int ID) {
        valueListener.setNewValue(null, name, "users/" + getUID() + "/name", ID);
    }

    public void changePicOnDatabase(String imgUri, int ID) {
        valueListener.setNewValue(null, imgUri, "users/" + getUID() + "/imgUri", ID);
    }

    public void deleteUserFromDatabase(int ID) {
        valueListener.setNewValue(null, null, "users/" + getUID(), ID);
    }

    public String getUID() {
        return Firebaseuser.getUid();
    }

    public void updateName(final Object data, final String name, final int ID) {
        Firebaseuser.updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(name).build()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(activity, "Change succeeded", Toast.LENGTH_LONG).show();
                user.setName(name);
                changeNameOnDatabase(name, ID);
                listener.onSucceed(data, ID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Change failed", Toast.LENGTH_LONG).show();
                listener.onfailure(data, ID);
            }
        });
    }

    public void updatePic(final Object data, final String uri, final int ID) {
        Firebaseuser.updateProfile(new UserProfileChangeRequest.Builder().setPhotoUri(Uri.parse(uri)).build()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(activity, "Change succeeded", Toast.LENGTH_LONG).show();
                user.setImgUri(uri);
                changePicOnDatabase(uri, ID);
                listener.onSucceed(data, ID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Change failed", Toast.LENGTH_LONG).show();
                listener.onfailure(data, ID);
            }
        });
    }

    public void updatePassword(final Object data, String password, final int ID) {
        Firebaseuser.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(activity, "Change succeeded", Toast.LENGTH_LONG).show();
                listener.onSucceed(data, ID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Change failed", Toast.LENGTH_LONG).show();
                listener.onfailure(data, ID);
            }
        });
    }

    public String getName() {
        return Firebaseuser.getDisplayName();
    }

    public String getEmail() {
        return Firebaseuser.getEmail();
    }

    public String getPhotoUrl() {
        if (Firebaseuser.getPhotoUrl() == null)
            return "";
        return Firebaseuser.getPhotoUrl().toString();
    }

    public void setProfilePicTo(ImageView img) {
        Picasso.with(activity).load(getPhotoUrl()).into(img);
    }

    public void uploadImage(final Object data, final Bitmap image, final int ID) {
        if (image != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageReference = storage.getReference().child("images").child(getUID());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bytes = baos.toByteArray();
            UploadTask uploadTask = storageReference.putBytes(bytes);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
                    listener.onfailure(data, ID);
                }
            });
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    updatePic(data, taskSnapshot.getDownloadUrl().toString(), ID);


                }
            });
        } else
            updatePic(data, "", ID);
    }

    public void uploadImage(Object data, Uri image, boolean scale, final int ID) {
        Bitmap images = null;
        try {
            if (scale)
                images = scaleDown(MediaStore.Images.Media.getBitmap(activity.getContentResolver(), image), 500, true);
            else
                images = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), image);
        } catch (IOException e) {
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        uploadImage(data, images, ID);

    }

    public void SignOut() {
        auth.signOut();
    }

    public User createUser() {
        return user = new User(getName(), getEmail(), getUID(), getPhotoUrl());
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

    public void deleteUser(final Object data, final int ID) {
        Firebaseuser.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                deleteUserFromDatabase(ID);
                listener.onSucceed(data, ID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onfailure(data, ID);
            }
        });

    }

    @Override
    public Activity getMyActivity() {
        return activity;
    }

    @Override
    public void onSucceed(Object Data, int ID) {

    }

    @Override
    public void onfailure(Object data, int ID) {

    }

    public void setEmergencyNumber(String newNumber) {
        valueListener.setNewValue(null, newNumber, "patients/" + getUID() + "/emergencyNumber", 4);
    }

    public void setStreetAddress(String newStreetAddress, boolean doctorAccount) {
        if (doctorAccount)
            valueListener.setNewValue(null, newStreetAddress, "doctors/" + getUID() + "/streetAddress", 5);
        else
            valueListener.setNewValue(null, newStreetAddress, "patients/" + getUID() + "/streetAddress", 5);
    }

    public void setCity(String newCity, boolean doctorAccount) {
        if (doctorAccount)
            valueListener.setNewValue(null, newCity, "doctors/" + getUID() + "/city", 6);
        else
            valueListener.setNewValue(null, newCity, "patients/" + getUID() + "/city", 6);
    }

    public void setState(String newState, boolean doctorAccount) {
        if (doctorAccount)
            valueListener.setNewValue(null, newState, "doctors/" + getUID() + "/state", 7);
        else
            valueListener.setNewValue(null, newState, "patients/" + getUID() + "/state", 7);
    }

    public void setZipCode(String newZipCode, boolean doctorAccount) {
        if (doctorAccount)
            valueListener.setNewValue(null, newZipCode, "doctors/" + getUID() + "/zipCode", 8);
        else
            valueListener.setNewValue(null, newZipCode, "patients/" + getUID() + "/zipCode", 8);
    }

    public void setMobileNumber(String newMobileNumber, boolean doctorAccount) {
        if (doctorAccount)
            valueListener.setNewValue(null, newMobileNumber, "doctors/" + getUID() + "/mobileNumber", 9);
        else
            valueListener.setNewValue(null, newMobileNumber, "patients/" + getUID() + "/mobileNumber", 9);
    }

    public void setAge(String newAge, boolean doctorAccount) {
        if (doctorAccount)
            valueListener.setNewValue(null, Integer.parseInt(newAge), "doctors/" + getUID() + "/age", 9);
        else
            valueListener.setNewValue(null, Integer.parseInt(newAge), "patients/" + getUID() + "/age", 9);
    }

    public void setCopounsUsage(boolean newCopounsUsage, boolean doctorAccount) {
        if (doctorAccount)
            valueListener.setNewValue(null, newCopounsUsage, "doctors/" + getUID() + "/useCopouns", 10);
        else
            valueListener.setNewValue(null, newCopounsUsage, "patients/" + getUID() + "/useCopouns", 10);
    }
}
