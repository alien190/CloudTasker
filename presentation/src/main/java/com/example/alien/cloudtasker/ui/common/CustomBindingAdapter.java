package com.example.alien.cloudtasker.ui.common;

import com.example.alien.cloudtasker.ui.taskList.TaskListAdapter;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;
import toothpick.Scope;
import toothpick.Toothpick;

public class CustomBindingAdapter {
    @BindingAdapter("bind:scopeName")
    public static void setRecyclerViewTaskListSource(RecyclerView recyclerView, String scopeName) {
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


}
