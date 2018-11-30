package com.example.domain.service;

import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;

import java.util.List;

import io.reactivex.Maybe;

public class TaskServiceImpl implements ITaskService {

    private ITaskRepository mLocalRepository;
    private ITaskRepository mRemoteRepository;

    public TaskServiceImpl(ITaskRepository localRepository, ITaskRepository remoteRepository) {
        mLocalRepository = localRepository;
        mRemoteRepository = remoteRepository;
    }

    @Override
    public Maybe<List<DomainUser>> getUserList() {
        return mRemoteRepository.getUserList();
    }
}
