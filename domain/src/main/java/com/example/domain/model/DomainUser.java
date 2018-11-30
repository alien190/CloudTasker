package com.example.domain.model;


import java.util.Date;

public class DomainUser {

    private String mUserId;
    private String mDisplayName;
    private Type mType;
    private Date mLastLoginTime;

    public DomainUser(String userId, String displayName, Type type) {
        mUserId = userId;
        mDisplayName = displayName;
        mType = type;
    }

    public DomainUser() {
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public Type getType() {
        return mType;
    }

    public void setType(Type type) {
        mType = type;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String displayName) {
        mDisplayName = displayName;
    }

    public Date getLastLoginTime() {
        return mLastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        mLastLoginTime = lastLoginTime;
    }

    public enum Type {
        ADDED,
        MODIFIED,
        REMOVED
    }
}
