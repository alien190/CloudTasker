package com.example.alien.cloudtasker.ui.taskList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.cloudtasker.databinding.TaskListBinding;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import toothpick.Scope;
import toothpick.Toothpick;

public class TaskListFragment extends Fragment {
    private static final String SCOPE_NAME_KEY = "TaskListFragment.ScopeName";

    @Inject
    ITaskListViewModel mViewModel;

    private View mView;

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
        Scope scope = Toothpick.openScope("TaskList");
        Toothpick.inject(this, scope);

        TaskListBinding taskListBinding = TaskListBinding.inflate(inflater);
        taskListBinding.setScopeName("TaskList");

        mViewModel.getTaskDetailId().observe(this, this::showTaskDetail);
        taskListBinding.setVm(mViewModel);
        taskListBinding.setLifecycleOwner(this);
        mView = taskListBinding.getRoot();
        return mView;
    }


    private void showTaskDetail(String taskId) {
        if (taskId != null) {
            TaskListFragmentDirections.ActionTaskListFragmentToTaskDetailFragment action =
                    TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment(taskId);
            Navigation.findNavController(mView).navigate(action);
            mViewModel.getTaskDetailId().setValue(null);
        }
    }
}
