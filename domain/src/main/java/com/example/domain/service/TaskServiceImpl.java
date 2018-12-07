package com.example.domain.service;

import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TaskServiceImpl implements ITaskService {

    private ITaskRepository mLocalRepository;
    private ITaskRepository mRemoteRepository;
    private CompositeDisposable mDisposable;

    public TaskServiceImpl(ITaskRepository localRepository,
                           ITaskRepository remoteRepository) {
        mLocalRepository = localRepository;
        mRemoteRepository = remoteRepository;
        mDisposable = new CompositeDisposable();
        refreshUserList();
        refreshTaskList();
    }

    private void refreshTaskList() {
        mDisposable.add(mRemoteRepository.getTaskList()
                .doOnError(mRemoteRepository::cleanCacheIfNeed)
                .flatMap(domainTasks -> mLocalRepository.insertTasks(domainTasks).toFlowable())
                .subscribeOn(Schedulers.io())
                .subscribe((v) -> {
                }, Throwable::printStackTrace));
    }

    private void refreshUserList() {
        mDisposable.add(mRemoteRepository.getUserList()
                .doOnError(mRemoteRepository::cleanCacheIfNeed)
                .flatMap(domainUsers -> mLocalRepository.insertUsers(domainUsers).toFlowable())
                .subscribeOn(Schedulers.io())
                .subscribe((v) -> {
                }, Throwable::printStackTrace));
    }


    @Override
    public Completable updateUser(String userId, Map<String, Object> updateFieldsMap) {
        return mRemoteRepository.updateUser(userId, updateFieldsMap)
                //.doOnError(mRemoteRepository::cleanCacheIfNeed)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable updateTask(String taskId, Map<String, Object> updateFieldsMap) {
        return mRemoteRepository.updateTask(taskId, updateFieldsMap)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<List<DomainUser>> getUserList() {
        return mLocalRepository.getUserList().subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<List<DomainTask>> getTaskList() {
        return mLocalRepository.getTaskList().subscribeOn(Schedulers.io());
    }

    @Override
    public Single<DomainTask> getTaskById(String taskId) {
        return mLocalRepository.getTaskById(taskId).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable insertTask(DomainTask task) {
        List<DomainTask> domainTasks = new ArrayList<>();
        domainTasks.add(task);
        return mRemoteRepository.insertTasks(domainTasks).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<DomainUser> getLoggedUser() {
        return mLocalRepository.getLoggedUser().subscribeOn(Schedulers.io());
    }
}
