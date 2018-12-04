package com.example.domain.service;

import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface ITaskService {

    Completable updateUser(DomainUser user);

    Flowable<List<DomainUser>> getUserList();

    Flowable<List<DomainTask>> getTaskList();

    Completable updateTask(DomainTask task);

}
