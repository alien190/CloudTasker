package com.example.alien.cloudtasker.di.usersViewModel;

import com.example.alien.cloudtasker.IUsersViewModel;

import androidx.fragment.app.FragmentActivity;
import toothpick.config.Module;

public class UserViewModelModule extends Module {

    public UserViewModelModule(FragmentActivity fragmentActivity) {
        bind(FragmentActivity.class).toInstance(fragmentActivity);
        bind(IUsersViewModel.class).toProvider(UsersViewModelProvider.class).providesSingletonInScope();
        bind(UsersViewModelFactory.class).toProvider(UsersViewModelFactoryProvider.class).providesSingletonInScope();
    }
}
