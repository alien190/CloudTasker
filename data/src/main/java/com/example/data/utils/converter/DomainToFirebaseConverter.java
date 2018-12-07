package com.example.data.utils.converter;

import com.example.data.model.FirebaseTask;
import com.example.data.model.FirebaseUser;
import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;

public final class DomainToFirebaseConverter {
    public static FirebaseUser convertUser(DomainUser domainUser) {
        if (domainUser != null) {
            FirebaseUser firebaseUser = new FirebaseUser();
            firebaseUser.setUserName(domainUser.getDisplayName());
            firebaseUser.setPhotoUrl(domainUser.getPhotoUrl());
            return firebaseUser;
        } else {
            throw new IllegalArgumentException("domainUser can't be null");
        }
    }

    public static FirebaseTask convertTask(DomainTask domainTask) {
        if (domainTask != null) {
            FirebaseTask firebaseTask = new FirebaseTask();
            firebaseTask.setTaskId(domainTask.getTaskId());
            firebaseTask.setAuthorId(domainTask.getAuthorId());
            firebaseTask.setExecutorId(domainTask.getExecutorId());
            firebaseTask.setText(domainTask.getText());
            firebaseTask.setTitle(domainTask.getTitle());
            firebaseTask.setIsComplete(domainTask.isComplete());
            return firebaseTask;
        } else {
            throw new IllegalArgumentException("domainTask can't be null");
        }
    }
}
