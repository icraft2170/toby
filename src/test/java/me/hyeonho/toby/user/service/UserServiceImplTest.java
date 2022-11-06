package me.hyeonho.toby.user.service;

import me.hyeonho.toby.TestDaoFactory;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import org.springframework.transaction.PlatformTransactionManager;

import static me.hyeonho.toby.user.service.UserLevelUpgradePolicyImpl.MIN_LOGCOUNT_FOR_SILVER;
import static me.hyeonho.toby.user.service.UserLevelUpgradePolicyImpl.MIN_RECCOMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDaoFactory.class})
class UserServiceImplTest {
    @Autowired
    UserServiceImpl userServiceImpl;
    @Autowired
    UserDao userDao;

    @Autowired
    MailSender mailSender;

    @Autowired
    PlatformTransactionManager platformTransactionManager;


    List<User> users;

    @BeforeEach
    void setUp(){
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1",Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1,0),
                new User("joytouch","강명성","p2",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
                new User("erwins","신승한","",Level.SILVER,60, MIN_RECCOMEND_FOR_GOLD - 1),
                new User("madnite1","이상호","p4",Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD),
                new User("green","오민규","p5",Level.GOLD,100,Integer.MAX_VALUE)
        );
    }


    @Test
    void 업그레이드_레벨() throws Exception {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userServiceImpl.upgradeLevels();

        checkLevel(users.get(0), false);
        checkLevel(users.get(1), true);
        checkLevel(users.get(2), false);
        checkLevel(users.get(3), true);
        checkLevel(users.get(4), false);

    }

    @Test
    void 업그레이드_레벨_트랜잭션() throws Exception {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }


        userServiceImpl.upgradeLevels();

        checkLevel(users.get(1), true);
    }

    @Test
    void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4); //GOLD
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userServiceImpl.add(userWithLevel);
        userServiceImpl.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    void upgradeAllOrNothing() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }
        UserServiceImpl testUserServiceImpl = new TestUserServiceImpl(userDao, users.get(3).getId(), platformTransactionManager, mailSender);

        assertThrows(TestUserLevelUpgradePolicyException.class, testUserServiceImpl::upgradeLevels);
        checkLevel(users.get(1), false);
        checkLevel(users.get(3), false);
    }


    private void checkLevel(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }


    static class TestUserServiceImpl extends UserServiceImpl {

        public TestUserServiceImpl(UserDao userDao, String id, PlatformTransactionManager platformTransactionManager, MailSender mailSender) {
            super(userDao, new TestUserLevelUpgradePolicy(userDao, mailSender,id), platformTransactionManager);
        }

        @Override
        public UserDao getUserDao() {
            return super.getUserDao();
        }

        @Override
        public UserLevelUpgradePolicy getUserLevelUpgradePolicy() {
            return super.getUserLevelUpgradePolicy();
        }

        @Override
        public void upgradeLevels() throws Exception {
            super.upgradeLevels();
        }

        @Override
        public void add(User user) {
            super.add(user);
        }
    }

    static class TestUserLevelUpgradePolicy extends UserLevelUpgradePolicyImpl {
        private String id;

        public TestUserLevelUpgradePolicy(UserDao userDao, MailSender mailSender ,String id) {
            super(userDao, mailSender);
            this.id = id;
        }

        @Override
        public void upgradeLevel(User user) {
            if (user.getId().equals(id)) throw new TestUserLevelUpgradePolicyException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserLevelUpgradePolicyException extends RuntimeException{}
}