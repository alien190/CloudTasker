package com.example.alien.cloudtasker.di.usersViewModel;


import androidx.lifecycle.ViewModelProviders;

import com.example.alien.cloudtasker.UsersViewModel;

import javax.inject.Provider;

import androidx.fragment.app.FragmentActivity;

class UsersViewModelProvider implements Provider<UsersViewModel> {
    private FragmentActivity mActivity;
    private UsersViewModelFactory mFactory;

    public UsersViewModelProvider(FragmentActivity activity, UsersViewModelFactory factory) {
        mActivity = activity;
        mFactory = factory;
    }

    @Override
    public UsersViewModel get() {
        return ViewModelProviders.of(mActivity, mFactory).get(UsersViewModel.class);
    }
}
