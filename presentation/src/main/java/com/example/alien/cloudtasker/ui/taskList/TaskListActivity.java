package com.example.alien.cloudtasker.ui.taskList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alien.cloudtasker.R;
import com.example.alien.cloudtasker.di.taskList.TaskListModule;
import com.example.alien.cloudtasker.ui.auth.AuthActivity;
import com.example.alien.cloudtasker.ui.common.SingleFragmentActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import toothpick.Scope;
import toothpick.Toothpick;

public class TaskListActivity extends SingleFragmentActivity {

    private static final String SCOPE_NAME = "TaskList";
    private static final String PARENT_SCOPE_NAME_KEY = "TaskListActivity.ParenScopeName";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            AuthActivity.start(this);
            finish();
        }
        Intent intent = getIntent();
        String parentScopeName = intent.getStringExtra(PARENT_SCOPE_NAME_KEY);
        Scope scope;
        if (parentScopeName != null && !parentScopeName.isEmpty()) {
            scope = Toothpick.openScopes(parentScopeName, SCOPE_NAME);
        } else {
            scope = Toothpick.openScope(SCOPE_NAME);
        }
        scope.installModules(new TaskListModule(this, SCOPE_NAME));

    }

    @Override
    protected Fragment getFragment() {

        return scope.getInstance(TaskListFragment.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_list_menu, menu);
        return true;
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
        final Context context = this;
        AuthUI.getInstance()
                .signOut(context)
                .addOnCompleteListener(task -> {
                    AuthActivity.start(context);
                    finish();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toothpick.closeScope(SCOPE_NAME);
    }

    public static void start(Context context, String parentScopeName) {
        if (context != null) {
            Intent intent = new Intent(context, TaskListActivity.class);
            if (parentScopeName != null && !parentScopeName.isEmpty()) {
                intent.putExtra(PARENT_SCOPE_NAME_KEY, parentScopeName);
            }
            context.startActivity(intent);
        } else {
            throw new IllegalArgumentException("Context can't be null");
        }
    }
}
