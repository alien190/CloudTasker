package com.example.alien.cloudtasker.di.taskDetail;

import com.example.alien.cloudtasker.ui.taskDetail.ITaskDetailViewModel;
import com.example.alien.cloudtasker.ui.taskDetail.TaskDetailFragment;

import toothpick.config.Module;

public class TaskDetailModule extends Module {
    public TaskDetailModule(String userId) {
        bind(String.class).withName(ITaskDetailViewModel.TASK_ID).toInstance(userId);
        bind(ITaskDetailViewModel.class).toProvider(TaskDetailViewModelProvider.class).providesSingletonInScope();
        bind(TaskDetailViewModelFactory.class).toProvider(TaskDetailViewModelFactoryProvider.class).providesSingletonInScope();
        bind(TaskDetailFragment.class).toProviderInstance(new TaskDetailFragmentProvider());
    }
}
