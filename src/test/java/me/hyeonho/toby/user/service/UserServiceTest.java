package me.hyeonho.toby.user.service;

import me.hyeonho.toby.TestDaoFactory;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDaoFactory.class})
class UserServiceTest {
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserService userService;
    List<User> users;


    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User("bumjin", "박범진", "p1", Level.BASIC, 49, 0),
                new User("joytouch", "강명성", "p2", Level.BASIC, 50, 0),
                new User("erwins", "신승한", "p3", Level.SILVER, 60, 29),
                new User("madnite1", "이상호", "p4", Level.SILVER, 60, 30),
                new User("green", "오민규", "p5", Level.GOLD, 100, 100)
        );
    }

    @Test
    @DisplayName("빈 주입 여부 테스트")
    void bean() {
        assertThat(userService).isNotNull();
        assertThat(userDao).isNotNull();
    }

    @Test
    @DisplayName("전체 유저 레벨 업그레이드 배치 처리")
    void upgradeLevels() {
        userDao.deleteAll();
        for (User user : users) userDao.add(user);

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertThat(userUpdate.getLevel()).isEqualTo(expectedLevel);
    }
}