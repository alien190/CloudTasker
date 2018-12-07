package com.example.alien.cloudtasker.ui.userDialog;

import com.example.domain.model.DomainUser;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public interface IUserViewModel {
    MutableLiveData<List<DomainUser>> getUsers();
}
