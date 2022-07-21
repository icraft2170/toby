package me.hyeonho.toby.user.service;


import lombok.RequiredArgsConstructor;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.Level;
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
            Boolean changed = null;
            if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            } else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            }
            else if (user.getLevel() == Level.GOLD) { changed = false; }
            else { changed = false; }
            if (changed) { userDao.update(user); }
        }
    }

}
