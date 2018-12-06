package com.example.alien.cloudtasker.ui.taskDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.cloudtasker.R;
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
        View view = inflater.inflate(R.layout.fr_task_detail, container, false);
        return view;
    }

    private void injectToothpick() {
        try {
            Scope scope = Toothpick.openScopes("TaskList", SCOPE_NAME);
            String userId = TaskDetailFragmentArgs.fromBundle(getArguments()).getUserId();
            scope.installModules(new TaskDetailModule(userId));
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
