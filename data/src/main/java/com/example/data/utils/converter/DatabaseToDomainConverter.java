package com.example.data.utils.converter;

import com.example.data.model.DatabaseTask;
import com.example.data.model.DatabaseTaskWithUsers;
import com.example.data.model.DatabaseUser;
import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public final class DatabaseToDomainConverter {
    public static DomainUser convertUser(DatabaseUser databaseUser) {
        if (databaseUser != null) {
            DomainUser domainUser = new DomainUser();
            domainUser.setLastLoginTime(new Date(databaseUser.getLastLoginTime()));
            domainUser.setUserId(databaseUser.getUserId());
            domainUser.setDisplayName(databaseUser.getUserName());
            domainUser.setPhotoUrl(databaseUser.getPhotoUrl());
            return domainUser;
        } else {
            throw new IllegalArgumentException("databaseUser can't be null");
        }
    }

    public static List<DomainUser> convertUsers(List<DatabaseUser> databaseUsers) {
        if (databaseUsers != null) {
            List<DomainUser> domainUsers = new ArrayList<>();
            for (DatabaseUser databaseUser : databaseUsers) {
                try {
                    domainUsers.add(convertUser(databaseUser));
                } catch (Throwable throwable) {
                    Timber.d(throwable);
                }
            }
            return domainUsers;
        } else {
            throw new IllegalArgumentException("databaseUsers can't be null");
        }
    }

    public static DomainTask convertTask(DatabaseTask databaseTask) {
        if (databaseTask != null) {
            DomainTask domainTask = new DomainTask();
            domainTask.setTaskId(databaseTask.getTaskId());
            domainTask.setAuthorId(databaseTask.getAuthorId());
            domainTask.setExecutorId(databaseTask.getExecutorId());
            domainTask.setText(databaseTask.getText());
            domainTask.setTitle(databaseTask.getTitle());
            domainTask.setComplete(databaseTask.isComplete());
            return domainTask;
        } else {
            throw new IllegalArgumentException("databaseTask can't be null");
        }
    }

    public static List<DomainTask> convertTasks(List<DatabaseTask> databaseTasks) {
        if (databaseTasks != null) {
            List<DomainTask> domainTasks = new ArrayList<>();
            for (DatabaseTask databaseTask : databaseTasks) {
                try {
                    domainTasks.add(convertTask(databaseTask));
                } catch (Throwable throwable) {
                    Timber.d(throwable);
                }
            }
            return domainTasks;
        } else {
            throw new IllegalArgumentException("databaseTasks can't be null");
        }
    }

    public static List<DomainTask> convertTasksWithUsers(List<DatabaseTaskWithUsers> databaseTasks) {
        if (databaseTasks != null) {
            List<DomainTask> domainTasks = new ArrayList<>();
            for (DatabaseTaskWithUsers databaseTask : databaseTasks) {
                try {
                    domainTasks.add(convertTaskWithUsers(databaseTask));
                } catch (Throwable throwable) {
                    Timber.d(throwable);
                }
            }
            return domainTasks;
        } else {
            throw new IllegalArgumentException("databaseTasks can't be null");
        }
    }

    public static DomainTask convertTaskWithUsers(DatabaseTaskWithUsers databaseTask) {
        DomainTask domainTask = convertTask(databaseTask);
        domainTask.setAuthorName(databaseTask.getAuthorName());
        domainTask.setExecutorName(databaseTask.getExecutorName());
        return domainTask;
    }
}
