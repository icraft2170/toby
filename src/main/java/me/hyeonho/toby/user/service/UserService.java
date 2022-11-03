package me.hyeonho.toby.user.service;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;

import java.util.List;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@RequiredArgsConstructor
@Getter
public class UserService {
    private final UserDao userDao;
    private final UserLevelUpgradePolicy userLevelUpgradePolicy;

    private final DataSource dataSource;

    public void upgradeLevels() throws Exception {
        TransactionSynchronizationManager.initSynchronization();
        Connection connection = DataSourceUtils.getConnection(dataSource);
        connection.setAutoCommit(false);
        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if (userLevelUpgradePolicy.canUpgradeLevel(user)) {
                    userLevelUpgradePolicy.upgradeLevel(user);
                }
            }
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            DataSourceUtils.releaseConnection(connection, dataSource);
            TransactionSynchronizationManager.unbindResource(dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }
}
