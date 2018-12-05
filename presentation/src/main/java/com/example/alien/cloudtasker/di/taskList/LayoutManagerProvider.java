package com.example.alien.cloudtasker.di.taskList;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Provider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LayoutManagerProvider implements Provider<RecyclerView.LayoutManager> {
    private Context mContext;

    @Inject
    public LayoutManagerProvider(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.LayoutManager get() {
        return new LinearLayoutManager(mContext);
    }
}
