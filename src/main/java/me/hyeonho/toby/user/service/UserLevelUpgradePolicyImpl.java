package me.hyeonho.toby.user.service;

import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;

public class UserLevelUpgradePolicyImpl implements UserLevelUpgradePolicy{
  public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
  public static final int MIN_RECCOMEND_FOR_GOLD = 30;

  private final UserDao userDao;

  public UserLevelUpgradePolicyImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public void upgradeLevel(User user) {
    user.upgradeLevel();
    userDao.update(user);
  }

  @Override
  public boolean canUpgradeLevel(User user) {
    Level currentLevel = user.getLevel();
    switch (currentLevel) {
      case BASIC: return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
      case SILVER: return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
      case GOLD: return false;
      default: throw new IllegalStateException("Unknown Level: " + currentLevel);
    }
  }
}
