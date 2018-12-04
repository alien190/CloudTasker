package com.example.domain.service;

import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
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
    }

    private void refreshUserList() {
        mDisposable.add(mRemoteRepository.getUserList()
                .doOnError(mRemoteRepository::cleanCacheIfNeed)
                .flatMap(mLocalRepository::insertUsers)
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

}
