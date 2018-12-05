package com.example.alien.cloudtasker.di.taskList;


import com.example.alien.cloudtasker.ui.taskList.ITaskListViewModel;
import com.example.alien.cloudtasker.ui.taskList.TaskListViewModel;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

class TaskListViewModelProvider implements Provider<ITaskListViewModel> {
    private FragmentActivity mActivity;
    private TaskListViewModelFactory mFactory;

    @Inject
    public TaskListViewModelProvider(FragmentActivity activity, TaskListViewModelFactory factory) {
        mActivity = activity;
        mFactory = factory;
    }

    @Override
    public ITaskListViewModel get() {
        return ViewModelProviders.of(mActivity, mFactory).get(TaskListViewModel.class);
    }
}
