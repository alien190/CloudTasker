package com.example.alien.cloudtasker;

import com.example.domain.model.DomainUser;

public interface IUsersViewModel {
    void getUsers();
    void updateUser(DomainUser user);
}
