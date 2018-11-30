package com.example.alien.cloudtasker;

import android.app.Application;

import com.example.alien.cloudtasker.di.application.ApplicationModule;
import com.example.alien.cloudtasker.di.application.DatabaseModule;
import com.example.alien.cloudtasker.di.application.NetworkModule;

import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        Scope scope = Toothpick.openScope("Application");
        scope.installModules(new ApplicationModule(getApplicationContext()),
                new NetworkModule(),
                new DatabaseModule(getApplicationContext()));
    }
}
