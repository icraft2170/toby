package me.hyeonho.toby.user.service;

import me.hyeonho.toby.user.domain.User;

public interface UserLevelUpgradePolicy {
  void upgradeLevel(User user);

  boolean canUpgradeLevel(User user);
}
