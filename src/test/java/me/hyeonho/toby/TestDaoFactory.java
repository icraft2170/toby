package me.hyeonho.toby;

import me.hyeonho.toby.user.dao.JdbcContext;
import me.hyeonho.toby.user.dao.UserDaoJdbc;
import me.hyeonho.toby.user.service.*;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

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
    public UserDaoJdbc userDao(){
        return new UserDaoJdbc(dataSource());
    }

    @Bean
    public PlatformTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    @Bean
    public UserServiceImpl userServiceImpl() {
        return new UserServiceImpl(userDao(), mailSender());
    }

    @Bean
    public TxProxyFactoryBean userService(){
        return new TxProxyFactoryBean(
                userServiceImpl(),
                transactionManager(),
                "upgradeLevels",
                UserService.class
        );
    }
}
