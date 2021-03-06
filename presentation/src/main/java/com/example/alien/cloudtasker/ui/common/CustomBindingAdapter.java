package com.example.alien.cloudtasker.ui.common;

import com.example.alien.cloudtasker.ui.taskList.TaskListAdapter;
import com.example.alien.cloudtasker.ui.userDialog.IUserDialogCallback;
import com.example.domain.model.DomainTask;
import com.example.domain.model.DomainUser;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

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

    @BindingAdapter({"bind:taskList", "bind:itemClickListener"})
    public static void setRecyclerViewTaskListSourceAndListener(RecyclerView recyclerView,
                                                                List<DomainTask> domainTasks,
                                                                TaskListAdapter.IOnItemClickListener clickListener) {
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter instanceof TaskListAdapter) {
            ((TaskListAdapter) adapter).setClickListener(clickListener);
            ((TaskListAdapter) adapter).submitList(domainTasks);
        }
    }

    @BindingAdapter({"bind:userList", "bind:onUserClick"})
    public static void setChipGroupScopeName(ChipGroup chipGroup, List<DomainUser> users, IUserDialogCallback callback) {
        if (users != null) {
            chipGroup.removeAllViews();
            for (DomainUser user : users) {
                Chip chip = new Chip(chipGroup.getContext());
                chip.setText(user.getDisplayName());
                chip.setTag(user.getUserId());
                chip.setOnClickListener(v -> {
                    if (callback != null) {
                        callback.onUserClick((String) chip.getTag(), (String) chip.getText());
                    }
                });
                chipGroup.addView(chip);
            }
        }
    }


}
