package com.example.data.utils.converter;

import com.example.data.model.DatabaseUser;
import com.example.domain.model.DomainUser;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public final class DomainToDatabaseConverter {
    public static DatabaseUser convertUser(DomainUser domainUser) {
        if (domainUser != null) {
            DatabaseUser databaseUser = new DatabaseUser();
            databaseUser.setLastLoginTime(domainUser.getLastLoginTime().getTime());
            databaseUser.setUserId(domainUser.getUserId());
            databaseUser.setUserName(domainUser.getDisplayName());
            return databaseUser;
        } else {
            throw new IllegalArgumentException("domainUser can't be null");
        }
    }

    public static List<DatabaseUser> convertUsers(List<DomainUser> domainUsers) {
        if (domainUsers != null) {
            List<DatabaseUser> databaseUsers = new ArrayList<>();
            for (DomainUser domainUser : domainUsers) {
                try {
                    databaseUsers.add(convertUser(domainUser));
                } catch (Throwable throwable) {
                    Timber.d(throwable);
                }
            }
            return databaseUsers;
        } else {
            throw new IllegalArgumentException("domainUsers can't be null");
        }
    }
}
