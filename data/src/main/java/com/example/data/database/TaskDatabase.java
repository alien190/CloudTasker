package com.example.data.database;


import com.example.data.model.DatabaseUser;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DatabaseUser.class}, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    public abstract ITaskDao getTaskDao();
}
