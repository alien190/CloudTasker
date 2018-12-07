package com.example.data.repository;

import com.example.data.database.ITaskDao;
import com.example.data.utils.converter.DatabaseToDomainConverter;
import com.example.data.utils.converter.DomainToDatabaseConverter;
import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class TaskLocalRepository implements ITaskRepository {
    private ITaskDao mTaskDao;
    private DomainUser mUser;

    public TaskLocalRepository(ITaskDao taskDao, DomainUser user) {
        if (taskDao != null && user != null && user.getUserId() != null && !user.getUserId().isEmpty()) {
            mTaskDao = taskDao;
            mUser = user;
        } else {
            throw new IllegalArgumentException("taskDao && user can't be null or empty");
        }
    }

    @Override
    public Flowable<List<DomainUser>> getUserList() {
        return mTaskDao.getUsersLive()
                .map(DatabaseToDomainConverter::convertUsers)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public void cleanCacheIfNeed(Throwable throwable) {
        //do nothing
    }

    @Override
    public Completable updateUser(DomainUser user) {
        return Completable.error(getError());
    }

    @Override
    public Completable updateTask(DomainTask task) {
        return Completable.error(getError());
    }

    @Override
    public Completable insertUsers(List<DomainUser> users) {
        return Completable.fromRunnable(() -> {
            for (DomainUser user : users) {
                try {
                    Timber.d("insertUsers userId:%s, userName:%s, type:%s",
                            user.getUserId(), user.getDisplayName(), user.getType().toString());
                    switch (user.getType()) {
                        case ADDED: {
                            mTaskDao.insertUser(DomainToDatabaseConverter.convertUser(user));
                            break;
                        }
                        case MODIFIED: {
                            mTaskDao.insertUser(DomainToDatabaseConverter.convertUser(user));
                            break;
                        }
                        case REMOVED: {
                            mTaskDao.deleteUserById(user.getUserId());
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                } catch (Throwable throwable) {
                    Timber.d(throwable);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Completable insertTasks(List<DomainTask> domainTasks) {
        return Completable.fromRunnable(() -> {
            for (DomainTask task : domainTasks) {
                try {
                    Timber.d("insertTasks taskId:%s, taskTitle:%s, type:%s",
                            task.getTaskId(), task.getTitle(), task.getType().toString());
                    switch (task.getType()) {
                        case ADDED: {
                            mTaskDao.insertTask(DomainToDatabaseConverter.convertTask(task));
                            break;
                        }
                        case MODIFIED: {
                            mTaskDao.insertTask(DomainToDatabaseConverter.convertTask(task));
                            break;
                        }
                        case REMOVED: {
                            mTaskDao.deleteTaskById(task.getTaskId());
                            break;
                        }
                        default: {
                            break;
                        }
                    }
                } catch (Throwable throwable) {
                    Timber.d(throwable);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<List<DomainTask>> getTaskList() {
        return mTaskDao.getTasksWithUsersLive(mUser.getUserId())
                .map(DatabaseToDomainConverter::convertTasksWithUsers)
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<DomainTask> getTaskById(String taskId) {
        return mTaskDao.getTaskWithUsersById(taskId)
                .map(DatabaseToDomainConverter::convertTaskWithUsers)
                .subscribeOn(Schedulers.io());

    }

    @Override
    public Completable updateTask(String taskId, Map<String, Object> updateFieldsMap) {
        return Completable.error(getError());
    }

    @Override
    public Completable updateUser(String userId, Map<String, Object> updateFieldsMap) {
        return Completable.error(getError());
    }

    private Throwable getError() {
        return new Throwable("do nothing");
    }
}
