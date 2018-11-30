package com.example.data.repository;

import com.example.data.utils.converter.FireBaseToDomainConverter;
import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import durdinapps.rxfirebase2.RxFirestore;
import io.reactivex.Maybe;

public class TaskRemoteRepository implements ITaskRepository {
    private static final String USER_COLLECTION_NAME = "users";
    private static final String TASK_COLLECTION_NAME = "tasks";
    private FirebaseFirestore mDatabase;

    public TaskRemoteRepository(FirebaseFirestore database) {
        if (database != null) {
            mDatabase = database;
        } else {
            throw new IllegalArgumentException("database can't be null");
        }
    }

    @Override
    public Maybe<List<DomainUser>> getUserList() {
        CollectionReference collectionReference = mDatabase.collection(USER_COLLECTION_NAME);
        return RxFirestore.getCollection(collectionReference).map(this::mapUser);
    }

    private List<DomainUser> mapUser(QuerySnapshot queryDocumentSnapshots) {
        List<DomainUser> users = new ArrayList<>();
        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.getMetadata().hasPendingWrites())
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    users.add(FireBaseToDomainConverter.convertUser(documentChange));
                }
            }
        return users;
    }
}
