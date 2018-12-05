package com.example.alien.cloudtasker.di.taskList;

import com.example.alien.cloudtasker.ui.taskList.TaskListViewModel;
import com.example.domain.service.ITaskService;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

class TaskListViewModelFactory implements ViewModelProvider.Factory {
    private ITaskService mTrackService;

    public TaskListViewModelFactory(ITaskService trackService) {
        mTrackService = trackService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TaskListViewModel(mTrackService);
    }
}
