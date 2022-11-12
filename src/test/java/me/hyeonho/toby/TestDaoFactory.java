package me.hyeonho.toby;

import java.lang.reflect.Proxy;
import me.hyeonho.toby.learningtest.aop.factory_bean.MessageFactoryBean;
import me.hyeonho.toby.user.dao.JdbcContext;
import me.hyeonho.toby.user.dao.UserDaoJdbc;
import me.hyeonho.toby.user.service.DummyMailSender;
import me.hyeonho.toby.user.service.TransactionAdvice;
import me.hyeonho.toby.user.service.TransactionHandler;
import me.hyeonho.toby.user.service.UserLevelUpgradePolicy;
import me.hyeonho.toby.user.service.UserLevelUpgradePolicyImpl;
import me.hyeonho.toby.user.service.UserService;
import me.hyeonho.toby.user.service.UserServiceImpl;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
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
    public UserServiceImpl userServiceImpl() {
        return new UserServiceImpl(userDao(), userLevelUpgradePolicy());
    }

    @Bean
    public TransactionAdvice transactionAdvice() {
        return new TransactionAdvice(platformTransactionManager());
    }

    @Bean
    public NameMatchMethodPointcut transactionPointcut() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("upgrade*");
        return pointcut;
    }

    @Bean
    public PointcutAdvisor transactionAdvisor() {
        return new DefaultPointcutAdvisor(transactionPointcut(), transactionAdvice());
    }

    @Bean
    public ProxyFactoryBean userService(){
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(userServiceImpl());
        pfBean.addAdvisor(transactionAdvisor());
        return pfBean;
    }

    @Bean
    public MailSender mailSender() {
        DummyMailSender dummyMailSender = new DummyMailSender();
        return dummyMailSender;
    }

    @Bean
    public UserLevelUpgradePolicy userLevelUpgradePolicy(){return new UserLevelUpgradePolicyImpl(userDao(), mailSender()); }

    @Bean(value = "message")
    public MessageFactoryBean message() {
        return new MessageFactoryBean("Factory Bean");
    }
}
