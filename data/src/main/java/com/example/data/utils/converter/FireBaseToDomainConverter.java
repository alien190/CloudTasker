package com.example.data.utils.converter;

import com.example.domain.model.DomainUser;
import com.google.firebase.firestore.DocumentChange;

import timber.log.Timber;

public final class FireBaseToDomainConverter {
    public static DomainUser convertUser(DocumentChange documentChange) {
        DomainUser domainUser = new DomainUser();
        try {
            domainUser.setDisplayName(documentChange.getDocument().get("userName").toString());
            domainUser.setUserId(documentChange.getDocument().getId());
            domainUser.setType(DomainUser.Type.valueOf(documentChange.getType().name()));
        } catch (Throwable throwable) {
            Timber.d(throwable);
        }
        return domainUser;
    }
}
