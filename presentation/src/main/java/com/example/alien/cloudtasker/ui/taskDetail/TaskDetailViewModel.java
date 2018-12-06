package com.example.alien.cloudtasker.ui.taskDetail;

import com.example.alien.cloudtasker.ui.userDialog.IUserDialogCallback;
import com.example.domain.model.DomainTask;
import com.example.domain.service.ITaskService;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class TaskDetailViewModel extends ViewModel implements ITaskDetailViewModel, IUserDialogCallback {
    private String mTaskId;
    private ITaskService mTaskService;
    private MutableLiveData<String> mTaskTitle = new MutableLiveData<>();
    private MutableLiveData<String> mTaskText = new MutableLiveData<>();
    private MutableLiveData<String> mAuthorName = new MutableLiveData<>();
    private MutableLiveData<String> mExecutorName = new MutableLiveData<>();
    private MutableLiveData<Boolean> mIsComplete = new MutableLiveData<>();
    private CompositeDisposable mDisposable = new CompositeDisposable();
    private MutableLiveData<Boolean> mOnFinish = new MutableLiveData<>();
    private MutableLiveData<String> mOnShowUserDialogFragment = new MutableLiveData<>();
    private DomainTask mTask;
    private boolean isAuthorEditing;

    public TaskDetailViewModel(ITaskService TaskService, String userId) {
        mTaskService = TaskService;
        //mUserId = userId;
    }

    @Override
    public void loadTaskById(String taskId) {
        if (mTaskId == null || !mTaskId.equals(taskId)) {
            mTaskId = taskId;
            if (mTaskId.isEmpty()) {
                DomainTask task = new DomainTask();
                task.setTaskId("");
                task.setTitle("");
                task.setText("");
                task.setComplete(false);
                task.setAuthorName("");
                task.setExecutorName("");
                mDisposable.add(Single.just(task).subscribe(this::setTask));
            } else {
                mDisposable.add(mTaskService.getTaskById(mTaskId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(this::setTask, Timber::d));
            }
        }
    }

    private void setTask(DomainTask task) {
        if (task != null) {
            mTask = task;
            mTaskTitle.setValue(task.getTitle());
            mTaskText.setValue(task.getText());
            mIsComplete.setValue(task.isComplete());
            mAuthorName.setValue(task.getAuthorName());
            mExecutorName.setValue(task.getExecutorName());
        }
    }

    @Override
    public void updateTask() {
        mTask.setTitle(mTaskTitle.getValue());
        mTask.setText(mTaskText.getValue());
        mTask.setComplete(mIsComplete.getValue());
        mDisposable.add(mTaskService.updateTask(mTask)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> mOnFinish.setValue(true), Timber::d));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable.clear();
            mDisposable = null;
        }
    }

    @Override
    public void onAuthorClick() {
        isAuthorEditing = true;
        mOnShowUserDialogFragment.setValue(mTask.getAuthorId());
    }

    @Override
    public void onExecutorClick() {
        isAuthorEditing = false;
        mOnShowUserDialogFragment.setValue(mTask.getAuthorId());
    }

    @Override
    public void onUserClick(String userId, String userName) {
        if (isAuthorEditing) {
            mAuthorName.setValue(userName);
            mTask.setAuthorId(userId);
        } else {
            mExecutorName.setValue(userId);
            mTask.setExecutorId(userId);
        }
    }

    @Override
    public MutableLiveData<String> getTaskTitle() {
        return mTaskTitle;
    }

    @Override
    public void setTaskTitle(MutableLiveData<String> title) {
        mTaskTitle = title;
    }

    @Override
    public void setTaskTitle(String title) {
        mTaskTitle.setValue(title);
    }

    @Override
    public MutableLiveData<String> getTaskText() {
        return mTaskText;
    }

    @Override
    public MutableLiveData<String> getAuthorName() {
        return mAuthorName;
    }

    @Override
    public MutableLiveData<String> getExecutorName() {
        return mExecutorName;
    }

    @Override
    public MutableLiveData<Boolean> getIsComplete() {
        return mIsComplete;
    }

    public MutableLiveData<Boolean> getOnFinish() {
        return mOnFinish;
    }

    public MutableLiveData<String> getOnShowAuthorDialogFragment() {
        return mOnShowUserDialogFragment;
    }

}
