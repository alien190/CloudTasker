package com.example.alien.cloudtasker.di.taskDetail;

import com.example.alien.cloudtasker.ui.taskDetail.TaskDetailFragment;

import javax.inject.Provider;

public class TaskDetailFragmentProvider implements Provider<TaskDetailFragment> {
    @Override
    public TaskDetailFragment get() {
        return TaskDetailFragment.newInstance();
    }
}
