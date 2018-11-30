package com.example.alien.cloudtasker.di.application;

import com.example.domain.repository.ITaskRepository;
import com.example.domain.service.ITaskService;
import com.example.domain.service.TaskServiceImpl;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

class TaskServiceProvider implements Provider<ITaskService> {
    private ITaskRepository mTaskRepositoryLocal;
    private ITaskRepository mTaskRepositoryRemote;

    @Inject
    public TaskServiceProvider(@Named(ITaskRepository.LOCAL) ITaskRepository taskRepositoryLocal,
                               @Named(ITaskRepository.REMOTE) ITaskRepository taskRepositoryRemote) {
        mTaskRepositoryLocal = taskRepositoryLocal;
        mTaskRepositoryRemote = taskRepositoryRemote;
    }

    @Override
    public ITaskService get() {
        return new TaskServiceImpl(mTaskRepositoryLocal, mTaskRepositoryRemote);
    }

}
