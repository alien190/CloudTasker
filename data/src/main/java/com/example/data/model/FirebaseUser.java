package com.example.data.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class FirebaseUser {
    @ServerTimestamp
    Date lastLoginTime;
    private String userName;

    public FirebaseUser() {
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
