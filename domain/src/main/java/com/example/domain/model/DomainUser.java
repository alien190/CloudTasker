package com.example.domain.model;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DomainUser {

    private String mUserId;
    private String mDisplayName;
    private Type mType;
    private Date mLastLoginTime;
    private String mPhotoUrl;

    public DomainUser(String userId, String displayName, Type type, Date lastLoginTime, String photoUrl) {
        mUserId = userId;
        mDisplayName = displayName;
        mType = type;
        mLastLoginTime = lastLoginTime;
        mPhotoUrl = photoUrl;
    }

    public DomainUser() {
    }

    public DomainUser(DomainUser user) {
        mUserId = user.mUserId;
        mDisplayName = user.mDisplayName;
        mType = user.mType;
        mLastLoginTime = user.mLastLoginTime;
        mPhotoUrl = user.mPhotoUrl;
    }

    public Map<String, Object> diff(DomainUser updatedUser) {
        Map<String, Object> retMap = new HashMap<>();
        if ((mDisplayName == null && updatedUser.mDisplayName != null) || (mDisplayName != null && !mDisplayName.equals(updatedUser.mDisplayName))) {
            retMap.put("userName", updatedUser.mDisplayName);
        }

        if ((mPhotoUrl == null && updatedUser.mPhotoUrl != null) || (mPhotoUrl != null && !mPhotoUrl.equals(updatedUser.mPhotoUrl))) {
            retMap.put("photoUrl", updatedUser.mPhotoUrl);
        }
        return retMap;
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

    public String getPhotoUrl() {
        return mPhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        mPhotoUrl = photoUrl;
    }

    public enum Type {
        ADDED,
        MODIFIED,
        REMOVED
    }
}
