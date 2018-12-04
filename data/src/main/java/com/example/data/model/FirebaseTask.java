package com.example.data.model;

public class FirebaseTask {
    private String taskId;
    private String authorId;
    private String executorId;
    private String title;
    private String text;

    public FirebaseTask() {
    }

    public FirebaseTask(String taskId, String authorId, String executorId, String title, String text) {
        this.taskId = taskId;
        this.authorId = authorId;
        this.executorId = executorId;
        this.title = title;
        this.text = text;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
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
