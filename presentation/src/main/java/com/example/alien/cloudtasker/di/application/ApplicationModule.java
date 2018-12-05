package com.example.alien.cloudtasker.di.application;

import android.content.Context;

import toothpick.config.Module;

public class ApplicationModule extends Module {

    public ApplicationModule(Context context) {
        bind(Context.class).toInstance(context);
    }
}
