package com.example.alien.cloudtasker.di.taskList;

import com.example.domain.service.ITaskService;

import javax.inject.Inject;
import javax.inject.Provider;

class TaskListViewModelFactoryProvider implements Provider<TaskListViewModelFactory> {
    private ITaskService mTaskService;

    @Inject
    public TaskListViewModelFactoryProvider(ITaskService taskService) {
        mTaskService = taskService;
    }

    @Override
    public TaskListViewModelFactory get() {
        return new TaskListViewModelFactory(mTaskService);
    }
}
