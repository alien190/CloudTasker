package com.example.alien.cloudtasker.ui.taskList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.cloudtasker.R;
import com.example.alien.cloudtasker.databinding.TaskListBinding;
import com.example.alien.cloudtasker.ui.taskDetail.TaskDetailFragment;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import toothpick.Scope;
import toothpick.Toothpick;

public class TaskListFragment extends Fragment {
    private static final String SCOPE_NAME_KEY = "TaskListFragment.ScopeName";

    @Inject
    ITaskListViewModel mViewModel;

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
            String scopeName = args.getString(SCOPE_NAME_KEY);
            if (scopeName != null && !scopeName.isEmpty()) {
                TaskListBinding taskListBinding = TaskListBinding.inflate(inflater);
                taskListBinding.setScopeName(scopeName);
                Scope scope = Toothpick.openScope(scopeName);
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
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, TaskDetailFragment.newInstance())
                        .addToBackStack(TaskDetailFragment.class.getSimpleName())
                        .commit();
            }
            mViewModel.getTaskDetailId().setValue(null);
        }
    }
}
