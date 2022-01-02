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
    private final UserLevelUpgradePolicy upgradePolicy;

    public void upgradeLevels(){
        List<User> users = userDao.getAll();
        for (User user : users) {
            if(upgradePolicy.canUpgradeLevel(user)) {
                upgradePolicy.upgradeLevel(user);
            }
        }
    }


    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

}
