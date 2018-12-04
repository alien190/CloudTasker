package com.example.data.repository;

import android.app.ActivityManager;
import android.content.Context;

import com.example.data.model.FirebaseUser;
import com.example.data.utils.converter.DomainToFirebaseConverter;
import com.example.data.utils.converter.FireBaseToDomainConverter;
import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import durdinapps.rxfirebase2.RxFirestore;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import timber.log.Timber;

import static android.content.Context.ACTIVITY_SERVICE;

public class TaskRemoteRepository implements ITaskRepository {
    private static final String USER_COLLECTION_NAME = "users";
    private static final String TASK_COLLECTION_NAME = "tasks";
    private FirebaseFirestore mDatabase;
    private Context mContext;

    public TaskRemoteRepository(FirebaseFirestore database, Context context) {
        if (database != null && context != null) {
            mDatabase = database;
            mContext = context;
        } else {
            throw new IllegalArgumentException("database && context can't be null");
        }
    }

    @Override
    public Flowable<List<DomainUser>> getUserList() {
        Query query = mDatabase.collection(USER_COLLECTION_NAME);
        return RxFirestore.observeQueryRef(query).map(this::mapUser);
    }

    private List<DomainUser> mapUser(QuerySnapshot queryDocumentSnapshots) {
        List<DomainUser> users = new ArrayList<>();
        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.getMetadata().hasPendingWrites())
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    DomainUser user = FireBaseToDomainConverter.convertUser(documentChange);
                    if (user != null && user.getUserId() != null && !user.getUserId().isEmpty()) {
                        users.add(user);
                    }
                }
            }
        return users;
    }

    @Override
    public void cleanCacheIfNeed(Throwable throwable) {
        try {
            if (throwable instanceof FirebaseFirestoreException &&
                    ((FirebaseFirestoreException) throwable).getCode().name().equals("PERMISSION_DENIED"))
                ((ActivityManager) mContext.getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
        } catch (Throwable t) {
            Timber.d(t);
        }
    }

    @Override
    public Completable updateUser(DomainUser user) {
        if (user != null) {
            DocumentReference reference = mDatabase.collection(USER_COLLECTION_NAME).document(user.getUserId());
            FirebaseUser firebaseUser = DomainToFirebaseConverter.convertUser(user);
            return RxFirestore.setDocument(reference, firebaseUser);
        } else {
            return Completable.error(new IllegalArgumentException("user can't be null"));
        }
    }

    @Override
    public Flowable<Boolean> insertUsers(List<DomainUser> users) {
        return Flowable.error(getError());
    }

    private Throwable getError() {
        return new Throwable("do nothing");
    }
}
