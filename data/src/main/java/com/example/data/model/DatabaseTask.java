package com.example.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = DatabaseUser.class,
                parentColumns = "userId",
                childColumns = "authorId",
                onDelete = CASCADE),
        @ForeignKey(
                entity = DatabaseUser.class,
                parentColumns = "userId",
                childColumns = "executorId",
                onDelete = CASCADE)})
public class DatabaseTask {
    @PrimaryKey
    @NonNull
    private String taskId;
    private String authorId;
    private String executorId;
    private String title;
    private String text;

    public DatabaseTask() {
    }

    @NonNull
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(@NonNull String taskId) {
        this.taskId = taskId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getExecutorId() {
        return executorId;
    }

    public void setExecutorId(String executorId) {
        this.executorId = executorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
