package com.example.alien.cloudtasker.ui.taskList;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alien.cloudtasker.R;
import com.example.domain.model.DomainTask;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskListViewHolder extends RecyclerView.ViewHolder {
    private TextView mTitleTextView;
    private TextView mTextTextView;
    private ImageView mCompleteImageView;


    public TaskListViewHolder(@NonNull View itemView) {
        super(itemView);
        mTitleTextView = itemView.findViewById(R.id.tv_title);
        mTextTextView = itemView.findViewById(R.id.tv_text);
        mCompleteImageView = itemView.findViewById(R.id.iv_complete);
    }

    public void bind(DomainTask task) {
        if (task != null) {
            if (task.getTitle() != null) {
                mTitleTextView.setText(task.getTitle());
            }
            if (task.getText() != null) {
                mTextTextView.setText(task.getText());
            }
            mCompleteImageView.setVisibility(task.isComplete() ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
