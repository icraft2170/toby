package me.hyeonho.toby.user.service;


import me.hyeonho.toby.user.domain.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class UserServiceTx implements UserService{
  private final UserService userService;
  private final PlatformTransactionManager transactionManager;


  public UserServiceTx(UserService userService, PlatformTransactionManager transactionManager) {
    this.userService = userService;
    this.transactionManager = transactionManager;
  }

  @Override
  public void upgradeLevels() {
    TransactionStatus status = transactionManager
        .getTransaction(new DefaultTransactionDefinition());
    try {
      userService.upgradeLevels();
      transactionManager.commit(status);
    } catch (RuntimeException e) {
      transactionManager.rollback(status);
      throw e;
    }

  }

  @Override
  public void add(User user) {
    userService.add(user);
  }
}
