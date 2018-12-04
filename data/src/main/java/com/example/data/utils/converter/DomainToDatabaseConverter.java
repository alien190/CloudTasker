package com.example.data.utils.converter;

import com.example.data.model.DatabaseTask;
import com.example.data.model.DatabaseUser;
import com.example.domain.model.DomainTask;
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

    public static DatabaseTask convertTask(DomainTask domainTask) {
        if (domainTask != null) {
            DatabaseTask databaseTask = new DatabaseTask();
            databaseTask.setTaskId(domainTask.getTaskId());
            databaseTask.setAuthorId(domainTask.getAuthorId());
            databaseTask.setExecutorId(domainTask.getExecutorId());
            databaseTask.setText(domainTask.getText());
            databaseTask.setTitle(domainTask.getTitle());
            return databaseTask;
        } else {
            throw new IllegalArgumentException("domainTask can't be null");
        }
    }

    public static List<DatabaseTask> convertTasks(List<DomainTask> domainTasks) {
        if (domainTasks != null) {
            List<DatabaseTask> databaseTasks = new ArrayList<>();
            for (DomainTask domainTask : domainTasks) {
                try {
                    databaseTasks.add(convertTask(domainTask));
                } catch (Throwable throwable) {
                    Timber.d(throwable);
                }
            }
            return databaseTasks;
        } else {
            throw new IllegalArgumentException("domainTasks can't be null");
        }
    }
}
