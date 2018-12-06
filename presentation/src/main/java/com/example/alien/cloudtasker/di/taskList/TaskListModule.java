package com.example.alien.cloudtasker.di.taskList;


import com.example.alien.cloudtasker.ui.common.IChangeFragment;
import com.example.alien.cloudtasker.ui.taskList.ITaskListViewModel;
import com.example.alien.cloudtasker.ui.taskList.TaskListAdapter;
import com.example.alien.cloudtasker.ui.taskList.TaskListFragment;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import toothpick.config.Module;

public class TaskListModule extends Module {

    public TaskListModule(FragmentActivity fragmentActivity, String scopeName) {
        bind(TaskListFragment.class).toInstance(TaskListFragment.newInstance(scopeName));
        bind(FragmentActivity.class).toInstance(fragmentActivity);
        if (fragmentActivity instanceof IChangeFragment) {
            bind(IChangeFragment.class).toInstance((IChangeFragment) fragmentActivity);
        }
        bind(ITaskListViewModel.class).toProvider(TaskListViewModelProvider.class).providesSingletonInScope();
        bind(TaskListViewModelFactory.class).toProvider(TaskListViewModelFactoryProvider.class).providesSingletonInScope();
        bind(TaskListAdapter.class).toInstance(new TaskListAdapter());
        bind(RecyclerView.LayoutManager.class).toProvider(LayoutManagerProvider.class);
    }
}
