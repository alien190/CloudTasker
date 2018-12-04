package com.example.data.repository;

import com.example.data.database.ITaskDao;
import com.example.data.utils.converter.DatabaseToDomainConverter;
import com.example.data.utils.converter.DomainToDatabaseConverter;
import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class TaskLocalRepository implements ITaskRepository {
    private ITaskDao mTaskDao;

    public TaskLocalRepository(ITaskDao taskDao) {
        if (taskDao != null) {
            mTaskDao = taskDao;
        } else {
            throw new IllegalArgumentException("taskDao can't be null");
        }
    }

    @Override
    public Flowable<List<DomainUser>> getUserList() {
        return null;
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
    public Flowable<Boolean> insertUsers(List<DomainUser> users) {
        return Flowable.fromCallable(() -> {
            for (DomainUser user : users) {
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
            }
            return true;
        });
    }

    @Override
    public Flowable<Boolean> insertTasks(List<DomainTask> domainTasks) {
        return Flowable.fromCallable(() -> {
            for (DomainTask task : domainTasks) {
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
            }
            return true;
        });
    }

    @Override
    public Flowable<List<DomainTask>> getTaskList() {
        return mTaskDao.getTasksWithUsersLive()
                .map(DatabaseToDomainConverter::convertTasksWithUsers)
                .subscribeOn(Schedulers.io());
    }

    private Throwable getError() {
        return new Throwable("do nothing");
    }
}
