package com.example.alien.cloudtasker.di.taskDetail;

import com.example.alien.cloudtasker.ui.taskDetail.ITaskDetailViewModel;
import com.example.domain.service.ITaskService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

class TaskDetailViewModelFactoryProvider implements Provider<TaskDetailViewModelFactory> {
    private ITaskService mTaskService;
    private String mUserId;

    @Inject
    public TaskDetailViewModelFactoryProvider(ITaskService taskService, @Named(ITaskDetailViewModel.TASK_ID) String userId) {
        mTaskService = taskService;
        mUserId = userId;
    }

    @Override
    public TaskDetailViewModelFactory get() {
        return new TaskDetailViewModelFactory(mTaskService, mUserId);
    }
}
