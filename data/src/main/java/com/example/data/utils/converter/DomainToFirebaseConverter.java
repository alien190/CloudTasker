package com.example.data.utils.converter;

import com.example.data.model.FirebaseUser;
import com.example.domain.model.DomainUser;

public final class DomainToFirebaseConverter {
    public static FirebaseUser convertUser(DomainUser domainUser) {
        if (domainUser != null) {
            FirebaseUser firebaseUser = new FirebaseUser();
            firebaseUser.setUserName(domainUser.getDisplayName());
            return firebaseUser;
        } else {
            throw new IllegalArgumentException("domainUser can't be null");
        }
    }
}
