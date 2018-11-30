package com.example.alien.cloudtasker.di.application;

import android.content.Context;

import com.example.data.repository.TaskRemoteRepository;
import com.example.domain.repository.ITaskRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import toothpick.config.Module;


public class NetworkModule extends Module {
    private FirebaseFirestore mFirebaseFirestore;
    private ITaskRepository mRemoteRepository;

    public NetworkModule(Context context) {
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mRemoteRepository = new TaskRemoteRepository(mFirebaseFirestore, context);
        bind(ITaskRepository.class).withName(ITaskRepository.REMOTE).toInstance(mRemoteRepository);
    }
}
