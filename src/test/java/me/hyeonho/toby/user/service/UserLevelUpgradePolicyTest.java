package me.hyeonho.toby.user.service;

import static me.hyeonho.toby.user.service.UserLevelUpgradePolicyImpl.MIN_LOGCOUNT_FOR_SILVER;
import static me.hyeonho.toby.user.service.UserLevelUpgradePolicyImpl.MIN_RECCOMEND_FOR_GOLD;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
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

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDaoFactory.class})
class UserLevelUpgradePolicyTest {
  @Autowired
  UserDao userDao;

  @Autowired
  UserService userService;

  List<User> users;

  @BeforeEach
  void setUp(){
    users = Arrays.asList(
        new User("bumjin", "박범진", "p1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1,0),
        new User("joytouch","강명성","p2",Level.BASIC,MIN_LOGCOUNT_FOR_SILVER,0),
        new User("erwins","신승한","",Level.SILVER,60, MIN_RECCOMEND_FOR_GOLD - 1),
        new User("madnite1","이상호","p4",Level.SILVER,60,MIN_RECCOMEND_FOR_GOLD),
        new User("green","오민규","p5",Level.GOLD,100,Integer.MAX_VALUE)
    );
  }




}