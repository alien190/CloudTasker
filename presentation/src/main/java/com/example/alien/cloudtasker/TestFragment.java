package com.example.alien.cloudtasker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.domain.model.DomainUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import toothpick.Scope;
import toothpick.Toothpick;

public class TestFragment extends Fragment {
    private FirebaseUser mUser;

    @Inject
    IUsersViewModel mUsersViewModel;

    public static TestFragment newInstance() {

        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_test, container, false);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            Scope scope = Toothpick.openScope("SecondActivity");
            Toothpick.inject(this, scope);
            mUsersViewModel.updateUser(new DomainUser(mUser.getUid(), mUser.getDisplayName(), null));
            mUsersViewModel.getUsers();
        }
        return view;
    }

}
