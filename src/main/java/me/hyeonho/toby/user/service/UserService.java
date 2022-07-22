package me.hyeonho.toby.user.service;


import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    // Batch 처리?
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        for (User user : users) {
            if (user.canUpgradeLevel()) upgradeLevel(user);
        }
    }

    public void add(User user) {
        user.initialLevelSetting();
        userDao.add(user);
    }

    protected void upgradeLevel(User user) {
        user.nextLevel();
        userDao.update(user);
    }
}
