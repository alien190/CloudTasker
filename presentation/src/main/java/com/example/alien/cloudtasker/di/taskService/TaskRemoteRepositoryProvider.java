package com.example.alien.cloudtasker.di.taskService;

import android.content.Context;

import com.example.data.repository.TaskRemoteRepository;
import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class TaskRemoteRepositoryProvider implements Provider<TaskRemoteRepository> {
    private FirebaseFirestore mFirestore;
    private Context mContext;
    private DomainUser mUser;

    @Inject
    public TaskRemoteRepositoryProvider(FirebaseFirestore firestore, Context context, @Named(ITaskRepository.LOGGED_USER)DomainUser user) {
        mFirestore = firestore;
        mContext = context;
        mUser = user;
    }

    @Override
    public TaskRemoteRepository get() {
        return new TaskRemoteRepository(mFirestore, mContext, mUser);
    }
}
