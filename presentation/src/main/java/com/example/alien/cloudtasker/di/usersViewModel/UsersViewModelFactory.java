package com.example.alien.cloudtasker.di.usersViewModel;

import com.example.alien.cloudtasker.UsersViewModel;
import com.example.domain.service.ITaskService;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

class UsersViewModelFactory implements ViewModelProvider.Factory {
    private ITaskService mTrackService;

    public UsersViewModelFactory(ITaskService trackService) {
        mTrackService = trackService;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new UsersViewModel(mTrackService);
    }
}
