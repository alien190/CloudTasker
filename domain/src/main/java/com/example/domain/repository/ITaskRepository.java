package com.example.domain.repository;

import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ITaskRepository {
    String REMOTE = "REMOTE";
    String LOCAL = "LOCAL";
    String LOGGED_USER = "LoggedUser";

    Flowable<List<DomainUser>> getUserList();

    void cleanCacheIfNeed(Throwable throwable);

    Completable updateUser(DomainUser user);

    Completable updateUser(String userId, Map<String, Object> updateFieldsMap);

    Completable insertUsers(List<DomainUser> users);

    Completable insertTasks(List<DomainTask> domainTasks);

    Flowable<List<DomainTask>> getTaskList();

    Completable updateTask(DomainTask task);

    Completable updateTask(String taskId, Map<String,Object> updateFieldsMap);

    Single<DomainTask> getTaskById(String taskId);

    Single<DomainUser> getLoggedUser();
}
