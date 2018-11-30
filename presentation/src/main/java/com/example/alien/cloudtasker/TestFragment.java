package com.example.alien.cloudtasker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TestFragment extends Fragment {
    private static final String USER_COLLECTION_NAME = "users";
    private static final String TASK_COLLECTION_NAME = "tasks";
    private FirebaseFirestore mDatabase;
    private FirebaseUser mUser;
    private ListenerRegistration mRegistration;

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
            initFirestore();
            initListener();
        }
        return view;
    }

    private void initFirestore() {

        mDatabase = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        mDatabase.setFirestoreSettings(settings);
    }

    private void initListener() {
        mRegistration = mDatabase.collection(TASK_COLLECTION_NAME)
                .whereEqualTo("authorUserId", mUser.getUid())
                //.orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener(this::snapshotListener);

    }

    private void snapshotListener(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
        if (e != null) {
            return;
        }
        if (queryDocumentSnapshots != null && !queryDocumentSnapshots.getMetadata().hasPendingWrites())
            if (!queryDocumentSnapshots.isEmpty()) {
                for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
                }
            }
    }
}
