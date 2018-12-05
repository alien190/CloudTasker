package com.example.alien.cloudtasker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.alien.cloudtasker.di.taskService.DatabaseModule;
import com.example.alien.cloudtasker.di.taskService.NetworkModule;
import com.example.alien.cloudtasker.di.taskService.TaskServiceModule;
import com.example.alien.cloudtasker.di.usersViewModel.UserViewModelModule;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import toothpick.Scope;
import toothpick.Toothpick;

public class SecondActivity extends SingleFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            AuthActivity.start(this);
            finish();
        } else {
            Scope scope = Toothpick.openScopes("Application", "TaskService");
            scope.installModules(new TaskServiceModule(user.getUid()),
                    new NetworkModule(),
                    new DatabaseModule());
        }
    }

    @Override
    protected Fragment getFragment() {
        Scope scope = Toothpick.openScopes("TaskService", "SecondActivity");
        scope.installModules(new UserViewModelModule(this));
        return TestFragment.newInstance();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toothpick.closeScope("SecondActivity");
    }

    public static void start(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, SecondActivity.class);
            context.startActivity(intent);
        } else {
            throw new IllegalArgumentException("Context can't be null");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_second, menu);
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
}
