package com.example.alien.cloudtasker.ui.userDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.cloudtasker.databinding.UserDialogBinding;
import com.example.alien.cloudtasker.di.usersViewModel.UserViewModelModule;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import toothpick.Scope;
import toothpick.Toothpick;

public class UserDialogFragment extends BottomSheetDialogFragment implements IUserDialogCallback {
    private static final String SCOPE_NAME_KEY = "UserDialogFragment.ScopeName";
    private IUserDialogCallback mDialogCallback;

    public static UserDialogFragment newInstance(String scopeName) {

        Bundle args = new Bundle();
        args.putString(SCOPE_NAME_KEY, scopeName);
        UserDialogFragment fragment = new UserDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            String scopeName = args.getString(SCOPE_NAME_KEY);
            if (scopeName != null && !scopeName.isEmpty()) {
                UserDialogBinding userDialogBinding = UserDialogBinding.inflate(inflater);
                Scope scope = Toothpick.openScopes("TaskDetail", "Users");
                scope.installModules(new UserViewModelModule());
                userDialogBinding.setVm(scope.getInstance(IUserViewModel.class));
                mDialogCallback = scope.getInstance(IUserDialogCallback.class);
                userDialogBinding.setClickCallback(this);
                userDialogBinding.setLifecycleOwner(this);
                return userDialogBinding.getRoot();
            }
        }
        throw new IllegalArgumentException("Scope name can't be null or empty");
    }

    @Override
    public void onUserClick(String userId, String userName) {
        if (mDialogCallback != null) {
            mDialogCallback.onUserClick(userId, userName);
        }
        dismiss();
    }

    public static void showDialogFragment(Fragment fragment, String scopeName) {
        if (fragment != null && scopeName != null && !scopeName.isEmpty()) {
            FragmentManager fragmentManager = fragment.getFragmentManager();
            if (fragmentManager != null) {
                UserDialogFragment userDialogFragment = UserDialogFragment.newInstance(scopeName);
                userDialogFragment.show(fragmentManager, scopeName);
            } else {
                throw new RuntimeException("getFragmentManager() return null");
            }
        } else {
            throw new RuntimeException("fragment can't be null and scopeName can't be empty or null");
        }
    }

}
