package me.hyeonho.toby.user.service;

import me.hyeonho.toby.user.domain.User;

public interface UserLevelUpgradePolicy {
    boolean canUpgradeLevel(User user);
    void upgradeLevel(User user);
}
