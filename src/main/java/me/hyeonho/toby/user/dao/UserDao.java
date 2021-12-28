package me.hyeonho.toby.user.dao;

import lombok.NoArgsConstructor;
import me.hyeonho.toby.user.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import javax.sql.DataSource;
import java.sql.*;

@NoArgsConstructor
public class UserDao {
    private DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException{
        StatementStrategy addStatement = new AddStatement(user);
        jdbcContextWithStatementStrategy(addStatement);
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
        jdbcContextWithStatementStrategy(new DeleteAllStatement());
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

    public void jdbcContextWithStatementStrategy(StatementStrategy strategy) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;

        try {
            c = dataSource.getConnection();

            ps = strategy.makePreparedStatement(c);

            ps.executeUpdate();
        }catch (SQLException e){
            throw e;
        }finally {
            if (ps != null){
                try {
                    ps.close();
                }catch (SQLException e){
                }
            }
            if(c != null){
                try {
                    c.close();
                }catch (SQLException e){
                }
            }
        }
    }


}
