package com.example.alien.cloudtasker.di.usersViewModel;

import com.example.domain.service.ITaskService;

import javax.inject.Provider;

class UsersViewModelFactoryProvider implements Provider<UsersViewModelFactory> {
    private ITaskService mTaskService;

    public UsersViewModelFactoryProvider(ITaskService taskService) {
        mTaskService = taskService;
    }

    @Override
    public UsersViewModelFactory get() {
        return new UsersViewModelFactory(mTaskService);
    }
}
