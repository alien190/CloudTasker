package com.example.alien.cloudtasker.di.application;

import android.content.Context;

import com.example.domain.service.ITaskService;

import toothpick.config.Module;

public class ApplicationModule extends Module {
    public ApplicationModule(Context applicationContext) {
        bind(ITaskService.class).toProvider(TaskServiceProvider.class).providesSingletonInScope();

    }
}
