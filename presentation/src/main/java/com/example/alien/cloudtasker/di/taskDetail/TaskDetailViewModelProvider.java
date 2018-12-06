package com.example.alien.cloudtasker.di.taskDetail;


import com.example.alien.cloudtasker.ui.taskDetail.TaskDetailViewModel;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

class TaskDetailViewModelProvider implements Provider<TaskDetailViewModel> {
    private FragmentActivity mActivity;
    private TaskDetailViewModelFactory mFactory;

    @Inject
    public TaskDetailViewModelProvider(FragmentActivity activity,
                                       TaskDetailViewModelFactory factory) {
        mActivity = activity;
        mFactory = factory;
    }

    @Override
    public TaskDetailViewModel get() {
        return ViewModelProviders.of(mActivity, mFactory).get(TaskDetailViewModel.class);
    }
}
