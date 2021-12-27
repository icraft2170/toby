package me.hyeonho.toby.user.dao;

import me.hyeonho.toby.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class UserDaoTestTest {

    @Test
    void 스프링컨테이너_싱글톤_테스트(){
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dao3 = context.getBean("userDao", UserDao.class);
        UserDao dao4 = context.getBean("userDao", UserDao.class);

        System.out.println(dao3);
        System.out.println(dao4);

        assertThat(dao3).isEqualTo(dao4);
    }

    @Test
    void addAndGet() throws SQLException{
        ApplicationContext context =
                new AnnotationConfigApplicationContext(DaoFactory.class);

        UserDao dao = context.getBean("userDao", UserDao.class);
        User user = new User();
        user.setId("gyumee");
        user.setName("박성철");
        user.setPassword("springno1");

        dao.add(user);

        User user2 = dao.get(user.getId());

        assertThat(user2.getName()).isEqualTo(user.getName());
        assertThat(user2.getPassword()).isEqualTo(user.getPassword());
    }

}