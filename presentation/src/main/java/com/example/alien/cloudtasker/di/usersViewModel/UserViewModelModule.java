package com.example.alien.cloudtasker.di.usersViewModel;

import com.example.alien.cloudtasker.ui.userDialog.IUserViewModel;

import androidx.fragment.app.FragmentActivity;
import toothpick.config.Module;

public class UserViewModelModule extends Module {

    public UserViewModelModule() {
        bind(IUserViewModel.class).toProvider(UsersViewModelProvider.class).providesSingletonInScope();
        bind(UsersViewModelFactory.class).toProvider(UsersViewModelFactoryProvider.class).providesSingletonInScope();
    }
}
