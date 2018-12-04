package com.example.data.model;

public class DatabaseTaskWithUsers extends DatabaseTask {
    private String authorName;
    private String executorName;

    public DatabaseTaskWithUsers(String authorName, String executorName) {
        this.authorName = authorName;
        this.executorName = executorName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getExecutorName() {
        return executorName;
    }

    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }
}
