package me.hyeonho.toby.user.dao;

import me.hyeonho.toby.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

class CountingConnectionMakerTest {


    @Test
    void 데코레이터_패턴을_이용한_OCP를_지키고_기능추가() throws SQLException, ClassNotFoundException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(DaoFactory.class);

        UserDao dao = context.getBean("userDao", UserDao.class);

        User user1 = new User();
        user1.setId("whiteship");
        user1.setName("김종헌");
        user1.setPassword("종헌짱");

        User user2 = new User();
        user2.setId("abcd1234");
        user2.setName("이건우");
        user2.setPassword("건우짱");

        dao.add(user1);
        dao.add(user2);

        CountingConnectionMaker ccm
                = context.getBean("connectionMaker", CountingConnectionMaker.class);

        System.out.println("ccm.getCount() = " + ccm.getCount());
        assertThat(ccm.getCount()).isEqualTo(2);

    }

}