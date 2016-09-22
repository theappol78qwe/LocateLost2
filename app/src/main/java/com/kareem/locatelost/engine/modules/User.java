/*
 * Copyright (c) $today.year.kareem elsayed aly,no one has the authority to edit,delete,copy any part without my permission
 */

package com.kareem.locatelost.engine.modules;

import java.io.Serializable;

/**
 * Created by kareem on 9/11/2016.
 */

public class User implements Serializable {
    private String name;
    private String email;
    private String uid;
    private String imgUri;

    public User(String name, String email, String uid, String imgUri) {
        this.setName(name);
        this.setEmail(email);
        this.setUid(uid);
        this.setImgUri(imgUri);

    }

    public User() {
        this.setName("");
        this.setEmail("");
        this.setUid("");
        this.setImgUri("");

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }
}
