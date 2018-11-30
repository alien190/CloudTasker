package com.example.alien.cloudtasker.di.application;

import android.content.Context;

import com.example.data.database.TaskDatabase;
import com.example.data.repository.TaskLocalRepository;
import com.example.domain.repository.ITaskRepository;

import androidx.room.Room;
import toothpick.config.Module;

public class DatabaseModule extends Module {

    private TaskDatabase mTaskDatabase;
    private ITaskRepository mLocalRepository;

    public DatabaseModule(Context context) {
        mTaskDatabase = Room.databaseBuilder(context, TaskDatabase.class, "launch_database")
                .fallbackToDestructiveMigration()
                .build();
        mLocalRepository = new TaskLocalRepository(mTaskDatabase);
        bind(ITaskRepository.class).withName(ITaskRepository.LOCAL).toInstance(mLocalRepository);
    }
}
