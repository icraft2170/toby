package me.hyeonho.toby.user.service;


import lombok.RequiredArgsConstructor;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.User;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserDao userDao;
    private final MailSender mailSender;


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
        sendUpgradeEmail(user);
    }

    private void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("icraft2170@gmail.com");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자님의 등급이 " + user.getLevel().name());

        mailSender.send(mailMessage);
    }

}
