package me.hyeonho.toby.user.service;

import me.hyeonho.toby.user.domain.User;

public interface UserService {
    void add(User user);
    void upgradeLevels();
}
