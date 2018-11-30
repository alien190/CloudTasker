package com.example.alien.cloudtasker;

import com.example.domain.model.DomainUser;
import com.example.domain.service.ITaskService;

import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class UsersViewModel extends ViewModel implements IUsersViewModel {
    private ITaskService mTaskService;
    private CompositeDisposable mDisposable;

    public UsersViewModel(ITaskService taskService) {
        if (taskService != null) {
            mTaskService = taskService;
            mDisposable = new CompositeDisposable();
        } else {
            throw new IllegalArgumentException("taskService can't be null");
        }
    }

    @Override
    public void getUsers() {

    }

    @Override
    public void updateUser(DomainUser user) {
        mDisposable.add(mTaskService.updateUser(user).subscribe(() -> {
        }, Timber::d));
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
}
