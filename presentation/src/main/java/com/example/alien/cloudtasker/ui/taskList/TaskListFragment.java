package com.example.alien.cloudtasker.ui.taskList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.cloudtasker.R;
import com.example.alien.cloudtasker.databinding.TaskListBinding;
import com.example.alien.cloudtasker.di.taskList.TaskListModule;
import com.example.alien.cloudtasker.di.taskService.DatabaseModule;
import com.example.alien.cloudtasker.di.taskService.NetworkModule;
import com.example.alien.cloudtasker.di.taskService.TaskServiceModule;
import com.example.alien.cloudtasker.ui.auth.AuthActivity;
import com.example.domain.model.DomainUser;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import toothpick.Scope;
import toothpick.Toothpick;

public class TaskListFragment extends Fragment {

    private static final String SCOPE_NAME = "TaskList";

    @Inject
    ITaskListViewModel mViewModel;

    private View mView;
    private FirebaseUser mUser;

    public static TaskListFragment newInstance() {
        Bundle args = new Bundle();
        TaskListFragment fragment = new TaskListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null) {
            mView = inflater.inflate(R.layout.fr_empty, container, false);
        } else {
            toothpickOpen();
            initView(inflater);
        }
        return mView;
    }


    private void toothpickOpen() {
        DomainUser domainUser = new DomainUser();
        domainUser.setUserId(mUser.getUid());
        domainUser.setDisplayName(mUser.getDisplayName());
        domainUser.setPhotoUrl(mUser.getPhotoUrl() != null ? mUser.getPhotoUrl().toString() : "");

        Scope scope = Toothpick.openScopes("NavigationActivity", "TaskService");
        scope.installModules(new TaskServiceModule(domainUser),
                new NetworkModule(),
                new DatabaseModule());

        scope = Toothpick.openScopes("TaskService", SCOPE_NAME);
        scope.installModules(new TaskListModule());

        Toothpick.inject(this, scope);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        toothpickClose();
    }

    private void toothpickClose() {
        Toothpick.closeScope(SCOPE_NAME);
    }

    private void initView(@NonNull LayoutInflater inflater) {
        TaskListBinding taskListBinding = TaskListBinding.inflate(inflater);
        taskListBinding.setScopeName(SCOPE_NAME);

        mViewModel.getTaskDetailId().observe(this, this::showTaskDetail);
        taskListBinding.setVm(mViewModel);
        taskListBinding.setLifecycleOwner(this);
        mView = taskListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mUser == null) {
            startAuth();
        }
    }

    private void showTaskDetail(String taskId) {
        if (taskId != null) {
            TaskListFragmentDirections.ActionTaskListFragmentToTaskDetailFragment action =
                    TaskListFragmentDirections.actionTaskListFragmentToTaskDetailFragment(taskId);
            Navigation.findNavController(mView).navigate(action);
            mViewModel.getTaskDetailId().setValue(null);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.task_list_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mi_logout: {
                logout();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    private void logout() {
        final Context context = getContext();
        if (context != null) {
            AuthUI.getInstance().signOut(context);
        }
    }

    private void startAuth() {
        TaskListFragmentDirections.ActionTaskListFragmentToAuthFragment action =
                TaskListFragmentDirections.actionTaskListFragmentToAuthFragment();
        Navigation.findNavController(mView).navigate(action);
    }
}
