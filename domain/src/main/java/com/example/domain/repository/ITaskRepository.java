package com.example.domain.repository;

import com.example.domain.model.DomainUser;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface ITaskRepository {
    String REMOTE = "REMOTE";
    String LOCAL = "LOCAL";

    Flowable<List<DomainUser>> getUserList();

    void cleanCacheIfNeed(Throwable throwable);

    Completable updateUser(DomainUser user);

    Flowable<Boolean> insertUsers(List<DomainUser> users);
}
