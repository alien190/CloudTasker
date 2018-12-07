package com.example.data.repository;

import android.app.ActivityManager;
import android.content.Context;

import com.example.data.model.FirebaseTask;
import com.example.data.model.FirebaseUser;
import com.example.data.utils.converter.DomainToFirebaseConverter;
import com.example.data.utils.converter.FireBaseToDomainConverter;
import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import durdinapps.rxfirebase2.RxFirestore;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.MaybeSource;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.Context.ACTIVITY_SERVICE;

public class TaskRemoteRepository implements ITaskRepository {
    private static final String USER_COLLECTION_NAME = "users";
    private static final String TASK_COLLECTION_NAME = "tasks";
    private FirebaseFirestore mDatabase;
    private Context mContext;
    private DomainUser mUser;

    public TaskRemoteRepository(FirebaseFirestore database, Context context, DomainUser user) {
        if (database != null && context != null && user != null && user.getUserId() != null && !user.getUserId().isEmpty()) {
            mDatabase = database;
            mContext = context;
            mUser = user;
            // actualizeUser();
        } else {
            throw new IllegalArgumentException("database && context && userId can't be null or empty");
        }
    }

    private void actualizeUser() {
        DocumentReference reference = mDatabase.collection(USER_COLLECTION_NAME).document(mUser.getUserId());
        Disposable disposable = RxFirestore.getDocument(reference).map(FireBaseToDomainConverter::convertUser)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .flatMap(user -> updateUser(mUser.getUserId(), user.diff(mUser)).toMaybe())
                .subscribe((v) -> {
                }, Timber::d);
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
                        Timber.d("mapUser userId:%s, userName:%s, type:%s",
                                user.getUserId(), user.getDisplayName(), user.getType().toString());
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
        return Completable.error(getError());
    }

    @Override
    public Completable updateUser(String userId, Map<String, Object> updateFieldsMap) {
        if (updateFieldsMap != null && !updateFieldsMap.isEmpty() && userId != null && !userId.isEmpty()) {
            DocumentReference reference = mDatabase.collection(USER_COLLECTION_NAME).document(userId);
            return RxFirestore.updateDocument(reference, updateFieldsMap);
        } else {
            return Completable.error(new IllegalArgumentException("updateFieldsMap && userId can't be null or empty"));
        }
    }

    @Override
    public Completable insertUsers(List<DomainUser> users) {
        return Completable.error(getError());
    }

    @Override
    public Completable insertTasks(List<DomainTask> domainTasks) {
        if (domainTasks != null && !domainTasks.isEmpty()) {
            CompletableSource[] completableList = new CompletableSource[domainTasks.size()];
            for (int i = 0; i < domainTasks.size(); i++) {
                completableList[i] = insertTask(domainTasks.get(i));
            }
            return Completable.concatArray(completableList);
        } else {
            return Completable.error(new Throwable("domainTasks can't be null or empty"));
        }
    }

    public Completable insertTask(DomainTask task) {
        if (task != null) {
            DocumentReference reference = mDatabase.collection(TASK_COLLECTION_NAME).document();
            return RxFirestore.setDocument(reference, DomainToFirebaseConverter.convertTask(task));
        } else {
            return Completable.error(new IllegalArgumentException("task can't be null"));
        }
    }


    @Override
    public Flowable<List<DomainTask>> getTaskList() {
        Query queryAuthor = mDatabase.collection(TASK_COLLECTION_NAME).whereEqualTo("authorId", mUser.getUserId());
        Query queryExecutor = mDatabase.collection(TASK_COLLECTION_NAME).whereEqualTo("executorId", mUser.getUserId());
        return RxFirestore.observeQueryRef(queryAuthor).map(this::mapTask)
                .mergeWith(RxFirestore.observeQueryRef(queryExecutor).map(this::mapTask));
    }

    private List<DomainTask> mapTask(QuerySnapshot queryDocumentSnapshots) {
        List<DomainTask> tasks = new ArrayList<>();
        if (queryDocumentSnapshots != null) //&& !queryDocumentSnapshots.getMetadata().hasPendingWrites()
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                    DomainTask task = FireBaseToDomainConverter.convertTask(documentChange);
                    if (task != null && task.getTaskId() != null && !task.getTaskId().isEmpty()) {
                        tasks.add(task);
                        Timber.d("mapTask taskId:%s, taskTitle:%s, type:%s",
                                task.getTaskId(), task.getTitle(), task.getType().toString());
                    }
                }
            }
        return tasks;
    }

    @Override
    public Completable updateTask(String taskId, Map<String, Object> updateFieldsMap) {
        if (updateFieldsMap != null && !updateFieldsMap.isEmpty() && taskId != null && !taskId.isEmpty()) {
            DocumentReference reference = mDatabase.collection(TASK_COLLECTION_NAME).document(taskId);
            return RxFirestore.updateDocument(reference, updateFieldsMap);
        } else {
            return Completable.error(new IllegalArgumentException("updateFieldsMap && taskId can't be null or empty"));
        }
    }

    @Override
    public Completable updateTask(DomainTask task) {
        return Completable.error(getError());
    }

    @Override
    public Single<DomainTask> getTaskById(String taskId) {
        return Single.error(getError());
    }

    private Throwable getError() {
        return new Throwable("do nothing");
    }

    @Override
    public Single<DomainUser> getLoggedUser() {
        return Single.just(mUser).subscribeOn(Schedulers.io());
    }
}
