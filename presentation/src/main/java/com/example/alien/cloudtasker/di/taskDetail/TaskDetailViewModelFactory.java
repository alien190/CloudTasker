package com.example.alien.cloudtasker.di.taskDetail;

import com.example.alien.cloudtasker.ui.taskDetail.TaskDetailViewModel;
import com.example.alien.cloudtasker.ui.taskList.TaskListViewModel;
import com.example.domain.service.ITaskService;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

class TaskDetailViewModelFactory implements ViewModelProvider.Factory {
    private ITaskService mTrackService;
    private String mUserId;

    public TaskDetailViewModelFactory(ITaskService trackService, String userId) {
        mTrackService = trackService;
        mUserId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TaskDetailViewModel(mTrackService, mUserId);
    }
}
