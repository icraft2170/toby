package me.hyeonho.toby.user.service;

import lombok.RequiredArgsConstructor;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;

@RequiredArgsConstructor
public class UserLevelUpgradeBasicPolicy implements UserLevelUpgradePolicy {
    public static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    public static final int MIN_RECCOMEND_FOR_GOLD = 30;
    private final UserDao userDao;


    @Override
    public boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel) {
            case BASIC:
                return (user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER);
            case SILVER:
                return (user.getRecommend() >= MIN_RECCOMEND_FOR_GOLD);
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknow Level: " + currentLevel);
        }
    }


    @Override
    public void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }
}
