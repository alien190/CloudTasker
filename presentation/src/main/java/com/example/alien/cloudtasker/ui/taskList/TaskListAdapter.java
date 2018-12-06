package com.example.alien.cloudtasker.ui.taskList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alien.cloudtasker.R;
import com.example.domain.model.DomainTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class TaskListAdapter extends ListAdapter<DomainTask, TaskListViewHolder> {
    private IOnItemClickListener mClickListener;

    private static final DiffUtil.ItemCallback<DomainTask> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<DomainTask>() {
                @Override
                public boolean areItemsTheSame(@NonNull DomainTask oldItem, @NonNull DomainTask newItem) {
                    return oldItem.getTaskId() != null && oldItem.getTaskId().equals(newItem.getTaskId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull DomainTask oldItem, @NonNull DomainTask newItem) {
                    return oldItem.equals(newItem);
                }
            };

    public TaskListAdapter() {
        this(DIFF_CALLBACK);
    }

    public TaskListAdapter(@NonNull DiffUtil.ItemCallback<DomainTask> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public TaskListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.li_task, parent, false);
        return new TaskListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskListViewHolder holder, int position) {
        holder.bind(getItem(position), mClickListener);
    }

    public void setClickListener(IOnItemClickListener clickListener) {
        mClickListener = clickListener;
    }

    public interface IOnItemClickListener {
        void inItemClick(String userId);
    }
}
