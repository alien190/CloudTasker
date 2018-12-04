package com.example.domain.service;

import com.example.domain.model.DomainUser;

import io.reactivex.Completable;

public interface ITaskService {

    Completable updateUser(DomainUser user);
}
