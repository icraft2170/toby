package me.hyeonho.toby.user.service;

import me.hyeonho.toby.user.domain.User;

public interface UserService {
  void upgradeLevels() ;

  void add(User user);
}
