package me.hyeonho.toby;

import me.hyeonho.toby.user.dao.JdbcContext;
import me.hyeonho.toby.user.dao.UserDao;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

@Configuration
public class TestDaoFactory {

    @Bean
    public DataSource dataSource(){
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://localhost/toby");
        dataSourceBuilder.username("toby");
        dataSourceBuilder.password("see3470");
        return dataSourceBuilder.build();
    }

    @Bean
    public JdbcContext jdbcContext(){
        return new JdbcContext(dataSource());
    }

    @Bean
    public UserDao userDao(){
        return new UserDao(dataSource(),jdbcContext());
    }

}
