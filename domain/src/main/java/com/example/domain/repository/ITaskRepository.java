package com.example.domain.repository;

import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface ITaskRepository {
    String REMOTE = "REMOTE";
    String LOCAL = "LOCAL";
    String USER_ID = "USER_ID";

    Flowable<List<DomainUser>> getUserList();

    void cleanCacheIfNeed(Throwable throwable);

    Completable updateUser(DomainUser user);

    Completable insertUsers(List<DomainUser> users);

    Completable insertTasks(List<DomainTask> domainTasks);

    Flowable<List<DomainTask>> getTaskList();

    Completable updateTask(DomainTask task);
}
