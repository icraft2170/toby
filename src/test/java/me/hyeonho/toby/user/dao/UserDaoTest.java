package me.hyeonho.toby.user.dao;

import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
class UserDaoTest {
    private UserDao dao;
    private DataSource dataSource;
    private User user1;
    private User user2;
    private User user3;

    // 픽스처(fixture)
    @BeforeEach
    void setUp(){
        dataSource = getDataSource();
        dao = new UserDaoJdbc(dataSource);
        user1 = new User("gyumee", "박성철", "springno1", Level.BASIC,1,0);
        user2 = new User("leegw700", "이길원", "springno2",Level.SILVER,55,10);
        user3 = new User("bumjin", "박범진", "springno3",Level.GOLD,100,40);
    }

    private SingleConnectionDataSource getDataSource() {
        return new SingleConnectionDataSource(
                "jdbc:mysql://localhost/toby", "toby", "see3470", true
        );
    }


    @Test
    void addAndGet() throws SQLException{
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        User userGet1 = dao.get(user1.getId());
        checkSameUser(userGet1,user1);

        User userGet2 = dao.get(user2.getId());
        checkSameUser(userGet2,user2);
    }

    @Test
    void count_테스트() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        assertThat(dao.getCount()).isEqualTo(1);

        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);
    }

    @Test
    void getUser_실패() throws SQLException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
    }

    @Test
    void getAll() throws SQLException {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertThat(users0.size()).isEqualTo(0);


        dao.add(user1);
        List<User> users1 =dao.getAll();
        assertThat(users1.size()).isEqualTo(1);
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 =dao.getAll();
        assertThat(users2.size()).isEqualTo(2);
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));


        dao.add(user3);
        List<User> users3 =dao.getAll();
        assertThat(users3.size()).isEqualTo(3);
        checkSameUser(user3, users3.get(0));
        checkSameUser(user1, users3.get(1));
        checkSameUser(user2, users3.get(2));
    }




    private void checkSameUser(User user1, User user2) {
        assertThat(user1.getId()).isEqualTo(user2.getId());
        assertThat(user1.getName()).isEqualTo(user2.getName());
        assertThat(user1.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user1.getLevel()).isEqualTo(user2.getLevel());
        assertThat(user1.getLogin()).isEqualTo(user2.getLogin());
        assertThat(user1.getRecommend()).isEqualTo(user2.getRecommend());
    }

    @Test
    void duplicateKey(){
        dao.deleteAll();
        dao.add(user1);
        assertThrows(DataAccessException.class, () -> dao.add(user1));
    }
    @Test
    void sqlExceptionTranslate(){
        dao.deleteAll();

        try {
            dao.add(user1);
            dao.add(user1);
        }catch (DuplicateKeyException e){
            SQLException sqlEx = (SQLException) e.getRootCause();
            SQLExceptionTranslator set =
                    new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            assertThat(set.translate(null,null,sqlEx))
                    .isInstanceOf(DuplicateKeyException.class);
        }
    }

    @Test
    void 유저_수정() {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);

        user1.setName("오민규");
        user1.setPassword("spring6");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1,user1update);
        User user2same = dao.get(user2.getId());
        checkSameUser(user2,user2same);
    }
}