package com.example.alien.cloudtasker;

import com.example.domain.service.ITaskService;

import androidx.lifecycle.ViewModel;

public class UsersViewModel extends ViewModel implements IUsersViewModel {
    private ITaskService mTaskService;

    public UsersViewModel(ITaskService taskService) {
        mTaskService = taskService;
    }
}
