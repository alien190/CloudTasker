package com.example.data.repository;

import com.example.data.database.TaskDatabase;
import com.example.domain.model.DomainUser;
import com.example.domain.repository.ITaskRepository;

import java.util.List;

import io.reactivex.Maybe;

public class TaskLocalRepository implements ITaskRepository {
    private TaskDatabase mTaskDatabase;

    public TaskLocalRepository(TaskDatabase taskDatabase) {
        if (taskDatabase != null) {
            mTaskDatabase = taskDatabase;
        } else {
            throw new IllegalArgumentException("taskDatabase can't be null");
        }
    }

    @Override
    public Maybe<List<DomainUser>> getUserList() {
        return null;
    }
}
