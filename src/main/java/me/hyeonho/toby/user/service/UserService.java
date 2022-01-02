package me.hyeonho.toby.user.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class UserService {
    private final UserDao userDao;

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for (User user : users) {
            if(canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        }
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

    private boolean canUpgradeLevel(User user) {
        Level currentLevel = user.getLevel();
        switch (currentLevel){
            case BASIC:
                return (user.getLogin() >= 50);
            case SILVER:
                return (user.getRecommend() >= 30);
            case GOLD:
                return false;
            default:
                throw new IllegalArgumentException("Unknow Level: " + currentLevel);
        }
    }

    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

}
