package com.example.alien.cloudtasker.ui.taskList;

import com.example.domain.model.DomainTask;
import com.example.domain.service.ITaskService;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class TaskListViewModel extends ViewModel implements ITaskListViewModel {
    private ITaskService mTaskService;
    private CompositeDisposable mDisposable = new CompositeDisposable();

    private MutableLiveData<List<DomainTask>> mTaskList = new MutableLiveData<>();

    public TaskListViewModel(ITaskService taskService) {
        mTaskService = taskService;
        subscribeTaskList();
    }

    private void subscribeTaskList() {
        mDisposable.add(mTaskService.getTaskList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mTaskList::postValue, Timber::d));
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
    public MutableLiveData<List<DomainTask>> getTaskList() {
        return mTaskList;
    }
}
