package com.example.data.database;

import com.example.data.model.DatabaseTask;
import com.example.data.model.DatabaseUser;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface ITaskDao {
    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //void insertUsers(List<DatabaseUser> users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(DatabaseUser user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(DatabaseTask task);

    @Query("SELECT * FROM databaseuser")
    Single<List<DatabaseUser>> getUsers();

    @Query("SELECT * FROM databaseuser WHERE userId=:userId")
    Single<DatabaseUser> getUserById(String userId);

    @Query("DELETE FROM databaseuser WHERE userId=:userId")
    void deleteUserById(String userId);
}
