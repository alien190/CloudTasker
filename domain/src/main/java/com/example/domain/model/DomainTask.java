package com.example.domain.model;

public class DomainTask {
    private String taskId;
    private String authorId;
    private String executorId;
    private String title;
    private String text;
    private Type type;

    public DomainTask() {
    }

    public DomainTask(String taskId, String authorId, String executorId, String title, String text, Type type) {
        this.taskId = taskId;
        this.authorId = authorId;
        this.executorId = executorId;
        this.title = title;
        this.text = text;
        this.type = type;
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

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        ADDED,
        MODIFIED,
        REMOVED
    }
}
