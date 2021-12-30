package me.hyeonho.toby.user.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class DaoFactoryTest {

    @Test
    public void 동등성_테스트(){
        DaoFactory factory = new DaoFactory();
        UserDao dao1 = factory.userDao();
        UserDao dao2 = factory.userDao();

        System.out.println("dao1 = " + dao1);
        System.out.println("dao2 = " + dao2);
        Assertions.assertThat(dao1).isNotEqualTo(dao2);
    }

}