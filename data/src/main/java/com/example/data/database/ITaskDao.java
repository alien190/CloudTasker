package com.example.data.database;

import com.example.data.model.DatabaseTask;
import com.example.data.model.DatabaseTaskWithUsers;
import com.example.data.model.DatabaseUser;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface ITaskDao {
    //@Insert(onConflict = OnConflictStrategy.REPLACE)
    //void insertUsers(List<DatabaseUser> users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertUser(DatabaseUser user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTask(DatabaseTask task);

    @Query("SELECT * FROM databaseuser")
    Single<List<DatabaseUser>> getUsers();

    @Query("SELECT * FROM databaseuser")
    Flowable<List<DatabaseUser>> getUsersLive();

    @Query("SELECT * FROM databasetask")
    Flowable<List<DatabaseTask>> getTasksLive();

    @Query("SELECT * FROM databaseuser WHERE userId=:userId")
    Single<DatabaseUser> getUserById(String userId);

    @Query("DELETE FROM databaseuser WHERE userId=:userId")
    void deleteUserById(String userId);

    @Query("DELETE FROM databaseuser")
    void deleteAllUsers();

    @Query("DELETE FROM databasetask")
    void deleteAllTasks();

    @Query("DELETE FROM databasetask WHERE taskId=:taskId")
    void deleteTaskById(String taskId);

    @Query("SELECT databasetask.*, authorName, executorName " +
            "FROM databasetask " +
            "JOIN (SELECT userId, userName AS authorName FROM databaseuser) AS authors ON databasetask.authorId=authors.userId " +
            "JOIN (SELECT userId, userName AS executorName FROM databaseuser) AS executors ON databasetask.executorId=executors.userId " +
            "WHERE databasetask.authorId =:userId OR databasetask.executorId=:userId")
    Flowable<List<DatabaseTaskWithUsers>> getTasksWithUsersLive(String userId);

    @Query("SELECT databasetask.*, authorName, executorName " +
            "FROM databasetask " +
            "JOIN (SELECT userId, userName AS authorName FROM databaseuser) AS authors ON databasetask.authorId=authors.userId " +
            "JOIN (SELECT userId, userName AS executorName FROM databaseuser) AS executors ON databasetask.executorId=executors.userId " +
            "WHERE taskId=:taskId")
    Single<DatabaseTaskWithUsers> getTaskWithUsersById(String taskId);
}
