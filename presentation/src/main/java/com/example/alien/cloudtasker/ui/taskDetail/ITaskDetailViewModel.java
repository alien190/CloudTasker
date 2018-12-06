package com.example.alien.cloudtasker.ui.taskDetail;


import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

public interface ITaskDetailViewModel {
    String TASK_ID = "TASK_ID";

    void loadTaskById(String taskId);

    MutableLiveData<String> getTaskTitle();

    void setTaskTitle(MutableLiveData<String> title);

    void setTaskTitle(String title);

    MutableLiveData<String> getTaskText();

    MutableLiveData<String> getAuthorName();

    MutableLiveData<String> getExecutorName();

    MutableLiveData<Boolean> getIsComplete();

    void updateTask();

    MutableLiveData<Boolean> getFinish();
}
