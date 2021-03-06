package com.example.domain.model;

import java.util.HashMap;
import java.util.Map;

public class DomainTask {
    private String taskId;
    private String authorId;
    private String executorId;
    private String title;
    private String text;
    private Type type;
    private String authorName;
    private String executorName;
    private boolean isComplete;

    public DomainTask() {
    }

    public DomainTask(DomainTask task) {
        taskId = task.getTaskId();
        authorId = task.getAuthorId();
        executorId = task.getExecutorId();
        title = task.getTitle();
        text = task.getText();
        type = task.getType();
        authorName = task.getAuthorName();
        executorName = task.getExecutorName();
        isComplete = task.isComplete();
    }

    public Map<String, Object> diff(DomainTask updatedTask) {
        Map<String, Object> retMap = new HashMap<>();
        if (!authorId.equals(updatedTask.authorId)) {
            retMap.put("authorId", updatedTask.authorId);
        }
        if (!executorId.equals(updatedTask.executorId)) {
            retMap.put("executorId", updatedTask.executorId);
        }
        if (!title.equals(updatedTask.title)) {
            retMap.put("title", updatedTask.title);
        }
        if (!text.equals(updatedTask.text)) {
            retMap.put("text", updatedTask.text);
        }
        if (!isComplete== updatedTask.isComplete) {
            retMap.put("isComplete", updatedTask.isComplete);
        }

        return retMap;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DomainTask) {
            DomainTask task = (DomainTask) o;
            return taskId != null && taskId.equals(task.getTaskId())
                    && authorId != null && authorId.equals(task.authorId)
                    && executorId != null && executorId.equals(task.executorId)
                    && authorName != null && authorName.equals(task.authorName)
                    && executorName != null && executorName.equals(task.executorName)
                    && title != null && title.equals(task.title)
                    && text != null && text.equals(task.text)
                    && isComplete == task.isComplete;
        }
        return false;
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

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }


    public enum Type {
        ADDED,
        MODIFIED,
        REMOVED
    }


}
