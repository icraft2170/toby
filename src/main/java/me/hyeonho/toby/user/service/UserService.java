package me.hyeonho.toby.user.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class UserService {
    private final UserDao userDao;
    private final UserLevelUpgradePolicy upgradePolicy;
    private final DataSource dataSource;

    public void upgradeLevels() throws Exception {
        DataSourceTransactionManager transactionManager =
                new DataSourceTransactionManager(dataSource);

        TransactionStatus status =
                transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if(upgradePolicy.canUpgradeLevel(user)) {
                    upgradePolicy.upgradeLevel(user);
                }
            }
            transactionManager.commit(status); // 트랜잭션 커밋
        }catch (RuntimeException e) {
            transactionManager.rollback(status); // 트랜잭션 롤백
            throw e;
        }
    }


    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

}
