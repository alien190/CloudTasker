package com.example.alien.cloudtasker.di.navigation;

import androidx.fragment.app.FragmentActivity;
import toothpick.config.Module;

public class NavigationActivityModule extends Module {

    public NavigationActivityModule(FragmentActivity fragmentActivity) {
        bind(FragmentActivity.class).toInstance(fragmentActivity);
    }
}
