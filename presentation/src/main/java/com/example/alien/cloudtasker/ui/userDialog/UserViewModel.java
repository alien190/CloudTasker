package com.example.alien.cloudtasker.ui.userDialog;

import com.example.domain.model.DomainUser;
import com.example.domain.service.ITaskService;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

public class UserViewModel extends ViewModel implements IUserViewModel {
    private ITaskService mTaskService;
    private CompositeDisposable mDisposable;
    private MutableLiveData<List<DomainUser>> mUsers = new MutableLiveData<>();

    public UserViewModel(ITaskService taskService) {
        if (taskService != null) {
            mTaskService = taskService;
            mDisposable = new CompositeDisposable();
            mDisposable.add(mTaskService.getUserList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mUsers::postValue, Timber::d));
        } else {
            throw new IllegalArgumentException("taskService can't be null");
        }
    }

    @Override
    public MutableLiveData<List<DomainUser>> getUsers() {
        return mUsers;
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
