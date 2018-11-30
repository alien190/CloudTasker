package com.example.data.database;

import com.example.data.model.DatabaseUser;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import io.reactivex.Flowable;

@Dao
public interface ITaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<DatabaseUser> users);
}
