package com.example.alien.cloudtasker.di.taskService;


import com.example.data.database.ITaskDao;
import com.example.domain.repository.ITaskRepository;

import toothpick.config.Module;

public class DatabaseModule extends Module {

    public DatabaseModule() {
        bind(ITaskDao.class).toProvider(TaskDaoProvider.class).providesSingletonInScope();
        bind(ITaskRepository.class).withName(ITaskRepository.LOCAL)
                .toProvider(TaskLocalRepositoryProvider.class).providesSingletonInScope();
    }
}
