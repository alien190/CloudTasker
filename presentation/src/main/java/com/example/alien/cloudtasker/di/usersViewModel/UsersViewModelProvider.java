package com.example.alien.cloudtasker.di.usersViewModel;


import androidx.lifecycle.ViewModelProviders;

import com.example.alien.cloudtasker.ui.userDialog.UserViewModel;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.fragment.app.FragmentActivity;

class UsersViewModelProvider implements Provider<UserViewModel> {
    private FragmentActivity mActivity;
    private UsersViewModelFactory mFactory;

    @Inject
    public UsersViewModelProvider(FragmentActivity activity, UsersViewModelFactory factory) {
        mActivity = activity;
        mFactory = factory;
    }

    @Override
    public UserViewModel get() {
        return ViewModelProviders.of(mActivity, mFactory).get(UserViewModel.class);
    }
}
