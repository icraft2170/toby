package me.hyeonho.toby.user.service;


import lombok.RequiredArgsConstructor;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.User;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;
    private final PlatformTransactionManager transactionManager;

    // Batch 처리?
    public void upgradeLevels() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (user.canUpgradeLevel()) upgradeLevel(user);
            }
            transactionManager.commit(status);
        } catch (RuntimeException exception) {
            transactionManager.rollback(status);
            throw exception;
        }
    }

    public void add(User user) {
        user.initialLevelSetting();
        userDao.add(user);
    }

    protected void upgradeLevel(User user) {
        user.nextLevel();
        userDao.update(user);
    }
}
