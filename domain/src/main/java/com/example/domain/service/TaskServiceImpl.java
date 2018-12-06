package com.example.domain.service;

import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;

import org.reactivestreams.Publisher;

import java.util.List;

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
    public Completable updateUser(DomainUser user) {
        return mRemoteRepository.updateUser(user)
                //.doOnError(mRemoteRepository::cleanCacheIfNeed)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable updateTask(DomainTask task) {
        return mRemoteRepository.updateTask(task)
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
}
