package me.hyeonho.toby.user.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.hyeonho.toby.user.dao.UserDao;
import me.hyeonho.toby.user.domain.Level;
import me.hyeonho.toby.user.domain.User;
import org.springframework.jdbc.datasource.DataSourceUtils;
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
        TransactionSynchronizationManager.initSynchronization(); // 동기화 작업 초기화
        Connection connection = DataSourceUtils.getConnection(dataSource);
        connection.setAutoCommit(false);
        try {
            List<User> users = userDao.getAll();
            for (User user : users) {
                if(upgradePolicy.canUpgradeLevel(user)) {
                    upgradePolicy.upgradeLevel(user);
                }
            }
            connection.commit();
        }catch (Exception e){
            connection.rollback();
            throw e;
        }finally {
            DataSourceUtils.releaseConnection(connection,dataSource); // 커넥션 닫기
            TransactionSynchronizationManager.unbindResource(this.dataSource);
            TransactionSynchronizationManager.clearSynchronization();
        }
    }


    public void add(User user) {
        if(user.getLevel() == null) user.setLevel(Level.BASIC);
        userDao.add(user);
    }

}
