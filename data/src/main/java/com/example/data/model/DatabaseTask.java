package com.example.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.NO_ACTION;

//@Entity(foreignKeys = {
//        @ForeignKey(
//                entity = DatabaseUser.class,
//                parentColumns = "userId",
//                childColumns = "authorId",
//                onDelete = CASCADE),
//        @ForeignKey(
//                entity = DatabaseUser.class,
//                parentColumns = "userId",
//                childColumns = "executorId",
//                onDelete = CASCADE)})
@Entity
public class DatabaseTask {
    @PrimaryKey
    @NonNull
    private String taskId;
    private String authorId;
    private String executorId;
    private String title;
    private String text;
    private boolean isComplete;

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

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
