package com.example.alien.cloudtasker.ui.taskDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.cloudtasker.databinding.TaskDetailBinding;
import com.example.alien.cloudtasker.di.taskDetail.TaskDetailModule;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;

public class TaskDetailFragment extends Fragment {
    private static final String SCOPE_NAME = "TaskDetail";
    @Inject
    ITaskDetailViewModel mViewModel;
    private String mTaskId;


    public static TaskDetailFragment newInstance() {

        Bundle args = new Bundle();
        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        injectToothpick();
        mViewModel.loadTaskById(mTaskId);
        TaskDetailBinding taskDetailBinding = TaskDetailBinding.inflate(inflater);
        taskDetailBinding.setVm(mViewModel);
        taskDetailBinding.setLifecycleOwner(this);
        mViewModel.getFinish().observe(this, this::onFinish);
        return taskDetailBinding.getRoot();
    }

    private void onFinish(Boolean isFinish) {
        if(isFinish!=null && isFinish) {
            mViewModel.getFinish().setValue(null);
        }
    }

    private void injectToothpick() {
        try {
            mTaskId = TaskDetailFragmentArgs.fromBundle(getArguments()).getTaskId();
            Scope scope = Toothpick.openScopes("TaskList", SCOPE_NAME);
            scope.installModules(new TaskDetailModule(mTaskId));
            Toothpick.inject(this, scope);
        } catch (Throwable throwable) {
            Timber.d(throwable);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toothpick.closeScope(SCOPE_NAME);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
