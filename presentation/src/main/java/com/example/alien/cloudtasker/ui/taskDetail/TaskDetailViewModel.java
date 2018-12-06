package com.example.alien.cloudtasker.ui.taskDetail;

import com.example.domain.service.ITaskService;

import androidx.lifecycle.ViewModel;

public class TaskDetailViewModel extends ViewModel implements ITaskDetailViewModel{
    private final String mUserId;
    private ITaskService mITaskService;

    public TaskDetailViewModel(ITaskService ITaskService, String userId) {
        mITaskService = ITaskService;
        mUserId = userId;
    }
}
