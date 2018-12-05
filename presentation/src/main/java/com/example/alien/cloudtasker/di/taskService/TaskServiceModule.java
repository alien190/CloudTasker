package com.example.alien.cloudtasker.di.taskService;

import com.example.domain.repository.ITaskRepository;
import com.example.domain.service.ITaskService;

import toothpick.config.Module;

public class TaskServiceModule extends Module {

    public TaskServiceModule(String userId) {
        bind(ITaskService.class).toProvider(TaskServiceProvider.class).providesSingletonInScope();
        bind(String.class).withName(ITaskRepository.USER_ID).toInstance(userId);
    }
}
