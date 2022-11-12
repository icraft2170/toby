package me.hyeonho.toby.user.service;

import java.lang.reflect.InvocationTargetException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionAdvice implements MethodInterceptor {

  private final PlatformTransactionManager transactionManager;

  public TransactionAdvice(PlatformTransactionManager transactionManager) {
    this.transactionManager = transactionManager;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    TransactionStatus status = transactionManager
        .getTransaction(new DefaultTransactionDefinition());

    try {
      Object ret = invocation.proceed();
      transactionManager.commit(status);
      return ret;
    } catch (RuntimeException e) {
      transactionManager.rollback(status);
      throw e;
    }
  }
}
