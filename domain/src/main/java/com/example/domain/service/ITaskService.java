package com.example.domain.service;

import com.example.domain.model.DomainUser;

import java.util.List;

import io.reactivex.Maybe;

public interface ITaskService {
    Maybe<List<DomainUser>> getUserList();
}
