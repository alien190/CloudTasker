package com.example.alien.cloudtasker.ui.taskDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.cloudtasker.R;
import com.example.alien.cloudtasker.di.taskDetail.TaskDetailModule;
import com.example.alien.cloudtasker.ui.common.IChangeFragment;

import javax.inject.Inject;

import androidx.annotation.IdRes;
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
        Scope scope = Toothpick.openScope(SCOPE_NAME);
        try {
            Toothpick.inject(this, scope);
        } catch (Throwable throwable) {
            Timber.d(throwable);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public static void start(String parentScopeName, String taskId) {
        try {
            Scope scope = Toothpick.openScopes(parentScopeName, SCOPE_NAME);
            scope.installModules(new TaskDetailModule(taskId));
            IChangeFragment changeFragment = scope.getInstance(IChangeFragment.class);
            Fragment fragment = scope.getInstance(TaskDetailFragment.class);
            changeFragment.changeFragment(fragment);
        } catch (Throwable throwable) {
            Timber.d(throwable);
        }
    }
}
