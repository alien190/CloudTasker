package com.example.alien.cloudtasker.di.taskService;

import com.example.domain.repository.ITaskRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import toothpick.config.Module;


public class NetworkModule extends Module {

    public NetworkModule() {
        bind(FirebaseFirestore.class).toInstance(FirebaseFirestore.getInstance());
        bind(ITaskRepository.class).withName(ITaskRepository.REMOTE).toProvider(TaskRemoteRepositoryProvider.class);
    }
}
