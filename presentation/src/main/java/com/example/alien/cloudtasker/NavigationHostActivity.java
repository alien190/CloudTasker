package com.example.alien.cloudtasker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alien.cloudtasker.di.taskList.TaskListModule;
import com.example.alien.cloudtasker.ui.auth.AuthActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import toothpick.Scope;
import toothpick.Toothpick;

public class NavigationHostActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_navigation_host);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            AuthActivity.start(this);
            finish();
        }
        Scope scope = Toothpick.openScopes("TaskService", "TaskList");
        scope.installModules(new TaskListModule(this, "TaskList"));
    }

    public static void start(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, NavigationHostActivity.class);
//            if (parentScopeName != null && !parentScopeName.isEmpty()) {
//                intent.putExtra(PARENT_SCOPE_NAME_KEY, parentScopeName);
//            }
            context.startActivity(intent);
        } else {
            throw new IllegalArgumentException("Context can't be null");
        }
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
        Toothpick.closeScope("TaskList");
    }
}
