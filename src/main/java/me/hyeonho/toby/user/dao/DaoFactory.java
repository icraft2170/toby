package me.hyeonho.toby.user.dao;

import me.hyeonho.toby.user.service.UserLevelUpgradeBasicPolicy;
import me.hyeonho.toby.user.service.UserLevelUpgradePolicy;
import me.hyeonho.toby.user.service.UserService;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DaoFactory {

    @Bean
    public DataSource dataSource(){
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://127.0.0.1:3306/toby?rewriteBatchedStatements=true&characterEncoding=UTF-8&serverTimezone=Asia/Seoul");
        dataSourceBuilder.username("toby");
        dataSourceBuilder.password("root");
        return dataSourceBuilder.build();
    }

    @Bean
    public JdbcContext jdbcContext(){
        return new JdbcContext(dataSource());
    }

    @Bean
    public UserDao userDao(){
        return new UserDaoJdbc(dataSource());
    }

    @Bean
    public UserService userService(){return new UserService(userDao(),userLevelUpgradePolicy(),dataSource()); }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy(){
        return new UserLevelUpgradeBasicPolicy(userDao());
    }
}
