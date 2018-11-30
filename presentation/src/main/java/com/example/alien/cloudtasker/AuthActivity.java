package com.example.alien.cloudtasker;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

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
            startSecondActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                startSecondActivity();
            } else {
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void startSecondActivity() {
        SecondActivity.start(this);
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
