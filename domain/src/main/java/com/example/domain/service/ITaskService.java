package com.example.domain.service;

import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ITaskService {

    Completable updateUser(String userId, Map<String, Object> updateFieldsMap);

    Flowable<List<DomainUser>> getUserList();

    Flowable<List<DomainTask>> getTaskList();

    Completable updateTask(String taskId, Map<String, Object> updateFieldsMap);

    Single<DomainTask> getTaskById(String taskId);

}
