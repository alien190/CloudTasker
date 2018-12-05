package com.example.alien.cloudtasker.ui.taskList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.cloudtasker.databinding.TaskListBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import toothpick.Scope;
import toothpick.Toothpick;

public class TaskListFragment extends Fragment {
    private static final String SCOPE_NAME_KEY = "TaskListFragment.ScopeName";

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
                taskListBinding.setVm(scope.getInstance(ITaskListViewModel.class));
                taskListBinding.setLifecycleOwner(this);
                return taskListBinding.getRoot();
            }
        }
        throw new IllegalArgumentException("scopeName can't be null ir empty");
    }
}
