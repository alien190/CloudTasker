package com.example.alien.cloudtasker.ui.common;

import com.example.alien.cloudtasker.ui.taskList.ITaskListViewModel;
import com.example.alien.cloudtasker.ui.taskList.TaskListAdapter;
import com.example.domain.model.DomainTask;

import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import toothpick.Scope;
import toothpick.Toothpick;

public class CustomBindingAdapter {
    @BindingAdapter("bind:scopeName")
    public static void setRecyclerViewTaskListScope(RecyclerView recyclerView, String scopeName) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            Scope scope = Toothpick.openScope(scopeName);
            initTaskListLayoutManager(scope, recyclerView);
            initTaskListAdapter(scope, recyclerView);
        }
    }

    private static void initTaskListLayoutManager(Scope scope, RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = scope.getInstance(RecyclerView.LayoutManager.class);
        recyclerView.setLayoutManager(layoutManager);
    }

    private static void initTaskListAdapter(Scope scope, RecyclerView recyclerView) {
        TaskListAdapter taskListAdapter = scope.getInstance(TaskListAdapter.class);
        recyclerView.setAdapter(taskListAdapter);
    }

    @BindingAdapter("bind:taskList")
    public static void setRecyclerViewTaskListSource(RecyclerView recyclerView, List<DomainTask> domainTasks) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof TaskListAdapter) {
            ((TaskListAdapter) adapter).submitList(domainTasks);
        }
    }

}
