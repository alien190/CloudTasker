package com.example.alien.cloudtasker.di.taskService;

import com.example.data.database.ITaskDao;
import com.example.data.repository.TaskLocalRepository;
import com.example.domain.repository.ITaskRepository;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;


public class TaskLocalRepositoryProvider implements Provider<TaskLocalRepository> {
    private ITaskDao mTaskDao;
    private String mUserId;

    @Inject
    public TaskLocalRepositoryProvider(ITaskDao taskDao, @Named(ITaskRepository.USER_ID) String userId) {
        mTaskDao = taskDao;
        mUserId = userId;
    }

    @Override
    public TaskLocalRepository get() {
        return new TaskLocalRepository(mTaskDao, mUserId);
    }
}
