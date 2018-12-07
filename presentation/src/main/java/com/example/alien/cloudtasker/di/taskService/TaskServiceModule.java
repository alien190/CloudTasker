package com.example.alien.cloudtasker.di.taskService;

import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;
import com.example.domain.service.ITaskService;

import toothpick.config.Module;

public class TaskServiceModule extends Module {

    public TaskServiceModule(DomainUser user) {
        bind(ITaskService.class).toProvider(TaskServiceProvider.class).providesSingletonInScope();
        bind(DomainUser.class).withName(ITaskRepository.LOGGED_USER).toInstance(user);
    }
}
