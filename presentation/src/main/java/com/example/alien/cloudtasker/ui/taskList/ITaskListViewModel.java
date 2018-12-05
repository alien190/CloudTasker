package com.example.alien.cloudtasker.ui.taskList;

import com.example.domain.model.DomainTask;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public interface ITaskListViewModel {
    MutableLiveData<List<DomainTask>> getTaskList();

    MutableLiveData<String> getTaskDetailId();

    void showTaskDetail(String taskId);
}
