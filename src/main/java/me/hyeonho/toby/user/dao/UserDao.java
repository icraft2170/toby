package me.hyeonho.toby.user.dao;

import lombok.NoArgsConstructor;
import me.hyeonho.toby.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import javax.sql.DataSource;
import java.sql.*;

@NoArgsConstructor
public class UserDao {
    private DataSource dataSource;
    private JdbcContext jdbcContext;

    public UserDao(DataSource dataSource, JdbcContext jdbcContext) {
        this.dataSource = dataSource;
        this.jdbcContext = jdbcContext;
    }

    public void add(final User user) throws SQLException{
        jdbcContext.workWithStatementStrategy(c -> {
            PreparedStatement ps =
                    c.prepareStatement("insert into users(id,name,password) values (?,?,?)");

            ps.setString(1, user.getId());
            ps.setString(2,user.getName());
            ps.setString(3,user.getPassword());

            return ps;
        });
    }

    public User get(String id) throws SQLException{
        Connection c = dataSource.getConnection();

        PreparedStatement ps =
                c.prepareStatement("select * from users where id = ?");
        ps.setString(1, id);

        ResultSet rs = ps.executeQuery();

        User user = null;
        if (rs.next()){
            user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
        }

        rs.close();
        ps.close();
        c.close();

        if(user == null){
            throw new EmptyResultDataAccessException(1);
        }

        return user;
    }

    public void deleteAll() throws SQLException {
        jdbcContext.executeSql("delete from users");
    }


    public int getCount() throws SQLException{
        Connection c =  null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            c = dataSource.getConnection();
            ps = c.prepareStatement("select count(*) from users");

            rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }catch (SQLException e){
            throw e;
        }finally {
            if(rs != null){
                try {
                    rs.close();
                }catch (SQLException e){}
            }
            if(ps != null){
                try {
                    ps.close();
                }catch (SQLException e){}
            }
            if(c != null){
                try {
                    c.close();
                }catch (SQLException e){}

            }
        }

    }


}
