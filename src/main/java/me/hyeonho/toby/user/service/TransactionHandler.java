package me.hyeonho.toby.user.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TransactionHandler implements InvocationHandler {

  private final Object target;
  private final PlatformTransactionManager transactionManager;
  private final String pattern;

  public TransactionHandler(Object target, PlatformTransactionManager transactionManager,
      String pattern) {
    this.target = target;
    this.transactionManager = transactionManager;
    this.pattern = pattern;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    if (method.getName().startsWith(pattern)) {
      return invokeInTransaction(method, args);
    } else {
      return method.invoke(target, args);
    }
  }


  public Object invokeInTransaction(Method method, Object[] args) throws Throwable {
    TransactionStatus status = transactionManager
        .getTransaction(new DefaultTransactionDefinition());
    try {
      Object ret = method.invoke(target, args);
      transactionManager.commit(status);
      return ret;
    } catch (InvocationTargetException e) {
      transactionManager.rollback(status);
      throw e.getTargetException();
    }

  }

}
