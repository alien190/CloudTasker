package com.example.alien.cloudtasker.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.alien.cloudtasker.R;
import com.example.alien.cloudtasker.di.navigation.NavigationActivityModule;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import toothpick.Scope;
import toothpick.Toothpick;

public class NavigationHostActivity extends AppCompatActivity {
    private static final String SCOPE_NAME = "NavigationActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_navigation_host);

        Scope scope = Toothpick.openScopes("Application", SCOPE_NAME);
        scope.installModules(new NavigationActivityModule(this));
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
    protected void onDestroy() {
        super.onDestroy();
        Toothpick.closeScope(SCOPE_NAME);
    }
}
