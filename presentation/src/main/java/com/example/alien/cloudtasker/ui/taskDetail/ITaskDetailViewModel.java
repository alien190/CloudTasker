package com.example.alien.cloudtasker.ui.taskDetail;


import androidx.lifecycle.MutableLiveData;

public interface ITaskDetailViewModel {
    String TASK_ID = "TASK_ID";

    MutableLiveData<String> getTaskTitle();

    MutableLiveData<String> getTaskText();

    MutableLiveData<String> getAuthorName();

    MutableLiveData<String> getExecutorName();

    MutableLiveData<Boolean> getIsComplete();

    MutableLiveData<Boolean> getOnFinish();

    MutableLiveData<String> getOnShowAuthorDialogFragment();

    void setTaskTitle(MutableLiveData<String> title);

    void setTaskTitle(String title);

    void loadTaskById(String taskId);

    void updateTask();

    void onAuthorClick();

    void onExecutorClick();
}
