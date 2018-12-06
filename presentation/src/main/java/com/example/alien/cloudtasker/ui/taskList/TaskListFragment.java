package com.example.alien.cloudtasker.ui.taskList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.cloudtasker.databinding.TaskListBinding;
import com.example.alien.cloudtasker.di.taskDetail.TaskDetailModule;
import com.example.alien.cloudtasker.ui.common.IChangeFragment;
import com.example.alien.cloudtasker.ui.taskDetail.TaskDetailFragment;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import timber.log.Timber;
import toothpick.Scope;
import toothpick.Toothpick;

public class TaskListFragment extends Fragment {
    private static final String SCOPE_NAME_KEY = "TaskListFragment.ScopeName";

    @Inject
    ITaskListViewModel mViewModel;
    private String mScopeName;

    public static TaskListFragment newInstance(String scopeName) {

        Bundle args = new Bundle();
        args.putString(SCOPE_NAME_KEY, scopeName);
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mScopeName = args.getString(SCOPE_NAME_KEY);
            if (mScopeName != null && !mScopeName.isEmpty()) {
                TaskListBinding taskListBinding = TaskListBinding.inflate(inflater);
                taskListBinding.setScopeName(mScopeName);
                Scope scope = Toothpick.openScope(mScopeName);
                Toothpick.inject(this, scope);
                mViewModel.getTaskDetailId().observe(this, this::showTaskDetail);
                taskListBinding.setVm(mViewModel);
                taskListBinding.setLifecycleOwner(this);
                return taskListBinding.getRoot();
            }
        }
        throw new IllegalArgumentException("scopeName can't be null or empty");
    }


    private void showTaskDetail(String taskId) {
        if (taskId != null) {
            TaskDetailFragment.start(mScopeName, taskId);
            mViewModel.getTaskDetailId().setValue(null);
        }
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
