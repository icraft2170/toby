package me.hyeonho.toby.user.dao;

import lombok.NoArgsConstructor;
import me.hyeonho.toby.user.domain.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

@NoArgsConstructor
public class UserDao {
    private JdbcTemplate jdbcTemplate;
    RowMapper<User> userMapper =(rs, rowNum) -> {
        return User.builder()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .password(rs.getString("password"))
                .build();
    };

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(final User user) throws SQLException{
        jdbcTemplate.update("insert into users(id,name,password) values (?,?,?)",
                user.getId(),user.getName(),user.getPassword());
    }

    public User get(String id) throws SQLException{
        return jdbcTemplate.queryForObject(
                "select * from users where id = ?",
                this.userMapper, id);
    }

    public void deleteAll() throws SQLException {
        jdbcTemplate.update("delete from users");
    }


    public int getCount() throws SQLException{
        return jdbcTemplate.queryForObject("select count(*) from users", Integer.class);
    }


    public List<User> getAll() {
        return jdbcTemplate.query("select * from users order by id", this.userMapper);
    }
}
