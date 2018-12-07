package com.example.data.utils.converter;

import com.example.data.model.FirebaseTask;
import com.example.data.model.FirebaseUser;
import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;

import timber.log.Timber;

public final class FireBaseToDomainConverter {
    public static DomainUser convertUser(DocumentChange documentChange) {
        DomainUser domainUser = new DomainUser();
        try {
            FirebaseUser firebaseUser = documentChange.getDocument().toObject(FirebaseUser.class);
            domainUser.setDisplayName(firebaseUser.getUserName());
            domainUser.setLastLoginTime(firebaseUser.getLastLoginTime());
            domainUser.setUserId(documentChange.getDocument().getId());
            domainUser.setType(DomainUser.Type.valueOf(documentChange.getType().name()));
            domainUser.setPhotoUrl(firebaseUser.getPhotoUrl());
        } catch (Throwable throwable) {
            Timber.d(throwable);
        }
        return domainUser;
    }

    public static DomainUser convertUser(DocumentSnapshot documentSnapshot) {
        if (documentSnapshot != null) {
            FirebaseUser firebaseUser = documentSnapshot.toObject(FirebaseUser.class);
            DomainUser domainUser = new DomainUser();
            domainUser.setUserId(documentSnapshot.getId());
            domainUser.setDisplayName(firebaseUser.getUserName());
            domainUser.setPhotoUrl(firebaseUser.getPhotoUrl());
            return domainUser;
        } else {
            throw new IllegalArgumentException("documentSnapshot can't be null");
        }
    }

    public static DomainTask convertTask(DocumentChange documentChange) {
        DomainTask domainTask = new DomainTask();
        try {
            FirebaseTask firebaseTask = documentChange.getDocument().toObject(FirebaseTask.class);
            domainTask.setTaskId(documentChange.getDocument().getId());
            domainTask.setAuthorId(firebaseTask.getAuthorId());
            domainTask.setExecutorId(firebaseTask.getExecutorId());
            domainTask.setTitle(firebaseTask.getTitle());
            domainTask.setText(firebaseTask.getText());
            domainTask.setComplete(firebaseTask.getIsComplete());
            domainTask.setType(DomainTask.Type.valueOf(documentChange.getType().name()));
        } catch (Throwable throwable) {
            Timber.d(throwable);
        }
        return domainTask;
    }
}
