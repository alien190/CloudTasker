package com.example.alien.cloudtasker.di.taskService;

import android.content.Context;

import com.example.data.database.ITaskDao;
import com.example.data.database.TaskDatabase;

import javax.inject.Provider;

import androidx.room.Room;

public class TaskDaoProvider implements Provider<ITaskDao> {
    private Context mContext;

    public TaskDaoProvider(Context context) {
        mContext = context;
    }

    @Override
    public ITaskDao get() {
        return Room.databaseBuilder(mContext, TaskDatabase.class, "task_database")
                .fallbackToDestructiveMigration()
                .build().getTaskDao();
    }
}
