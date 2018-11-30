package com.example.domain.repository;

import com.example.domain.model.DomainUser;

import java.util.List;

import io.reactivex.Maybe;

public interface ITaskRepository {
    String REMOTE = "REMOTE";
    String LOCAL = "LOCAL";

    Maybe<List<DomainUser>> getUserList();
}
