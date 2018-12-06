package com.example.alien.cloudtasker.ui.auth;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import toothpick.Scope;
import toothpick.Toothpick;

import android.os.Bundle;
import android.widget.Toast;

import com.example.alien.cloudtasker.NavigationHostActivity;
import com.example.alien.cloudtasker.R;
import com.example.alien.cloudtasker.di.taskService.DatabaseModule;
import com.example.alien.cloudtasker.di.taskService.NetworkModule;
import com.example.alien.cloudtasker.di.taskService.TaskServiceModule;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class AuthActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 77;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        auth();
    }

    private void auth() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user == null) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTheme(R.style.AppTheme_Auth)
                    .setLogo(R.drawable.hello)
                    .build(), RC_SIGN_IN);
        } else {
            startNavigationHostActivity(user.getUid());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                startNavigationHostActivity(user.getUid());
            } else {
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startNavigationHostActivity(String userId) {
        String scopeName = "TaskService";
        Scope scope = Toothpick.openScopes("Application", scopeName);
        scope.installModules(new TaskServiceModule(userId),
                new NetworkModule(),
                new DatabaseModule());
        NavigationHostActivity.start(this);
        finish();
    }

    public static void start(Context context) {
        if (context != null) {
            Intent intent = new Intent(context, AuthActivity.class);
            context.startActivity(intent);
        } else {
            throw new IllegalArgumentException("Context can't be null");
        }
    }
}

