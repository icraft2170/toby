package me.hyeonho.toby;

import java.lang.reflect.Proxy;
import me.hyeonho.toby.user.dao.JdbcContext;
import me.hyeonho.toby.user.dao.UserDaoJdbc;
import me.hyeonho.toby.user.service.DummyMailSender;
import me.hyeonho.toby.user.service.TransactionHandler;
import me.hyeonho.toby.user.service.UserLevelUpgradePolicy;
import me.hyeonho.toby.user.service.UserLevelUpgradePolicyImpl;
import me.hyeonho.toby.user.service.UserService;
import me.hyeonho.toby.user.service.UserServiceImpl;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TestDaoFactory {

    @Bean
    public DataSource dataSource(){
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("com.mysql.cj.jdbc.Driver");
        dataSourceBuilder.url("jdbc:mysql://localhost/toby");
        dataSourceBuilder.username("toby");
        dataSourceBuilder.password("root");
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
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }
    @Bean
    public UserService userService(){
        return (UserService) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class[]{UserService.class},
            new TransactionHandler(new UserServiceImpl(userDao(), userLevelUpgradePolicy()), platformTransactionManager(), "upgradeLevels")
        );
    }

    @Bean
    public MailSender mailSender() {
        DummyMailSender dummyMailSender = new DummyMailSender();
        return dummyMailSender;
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy(){return new UserLevelUpgradePolicyImpl(userDao(), mailSender()); }
}
