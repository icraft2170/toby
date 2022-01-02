package me.hyeonho.toby.user.service;

import me.hyeonho.toby.TestDaoFactory;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static me.hyeonho.toby.user.service.UserLevelUpgradeBasicPolicy.MIN_LOGCOUNT_FOR_SILVER;
import static me.hyeonho.toby.user.service.UserLevelUpgradeBasicPolicy.MIN_RECCOMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDaoFactory.class})
class UserServiceTest {
    @Autowired
    UserService userService;
    @Autowired
    UserDao userDao;
    @Autowired
    UserLevelUpgradePolicy upgradePolicy;


    List<User> users;

    @BeforeEach
    void setUp(){
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER - 1,0),
                new User("joytouch","강명성","p2",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
                new User("erwins","신승한","",Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD - 1),
                new User("madnite1","이상호","p4",Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD),
                new User("green","오민규","p5",Level.GOLD,100,Integer.MAX_VALUE)
        );
    }


    @Test
    void 업그레이드_레벨() {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    @Test
    void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4); //GOLD
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
    }

    @Test
    void 업그레이드_트랜잭션_테스트() {
        TestUserService testUserService = new TestUserService(this.userDao,this.upgradePolicy,users.get(3).getId());
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }
        try {
            testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        }catch (TestUserServiceException e){}

        checkLevelUpgraded(users.get(1),false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded){
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        }else{
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }



    static class TestUserService extends UserService{
        private String id;

        public TestUserService(UserDao userDao, UserLevelUpgradePolicy upgradePolicy, String id) {
            super(userDao, upgradePolicy);
            this.id = id;
        }


        protected void upgradeLevel(User user) {
            if(user.getId().equals(this.id)) throw  new TestUserServiceException();
            super.getUpgradePolicy().upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException{}
}