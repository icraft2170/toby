package me.hyeonho.toby.user.dao;

import lombok.NoArgsConstructor;
import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

@NoArgsConstructor
public class UserDaoJdbc implements UserDao {
    private JdbcTemplate jdbcTemplate;
    RowMapper<User> userMapper =(rs, rowNum) -> {
        return User.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .password(rs.getString("password"))
                .level(Level.valueOf(rs.getInt("level")))
                .login(rs.getInt("login"))
                .recommend(rs.getInt("recommend"))
                .build();
    };

    public UserDaoJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public void add(final User user){
        jdbcTemplate.update("insert into users(id,name,password,level,login,recommend) values (?,?,?,?,?,?)",
                user.getId(),user.getName(),user.getPassword(),user.getLevel().intValue(),user.getLogin(),user.getRecommend());
    }

    @Override
    public User get(String id) {
        return jdbcTemplate.queryForObject(
                "select * from users where id = ?",
                this.userMapper, id);
    }

    @Override
    public void deleteAll()  {
        jdbcTemplate.update("delete from users");
    }


    @Override
    public int getCount() {
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("select * from users order by id", this.userMapper);
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update("update users set  name=?,password=?,level =?,login=?,recommend=? where id = ?"
                            ,user.getName(),user.getPassword(),user.getLevel().intValue(),user.getLogin()
                            ,user.getRecommend(),user.getId());
    }
}
