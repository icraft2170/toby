package me.hyeonho.toby.learningtest.aop.factory_bean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import me.hyeonho.toby.TestDaoFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestDaoFactory.class})
public class FactoryBeanTest {

  @Autowired
  ApplicationContext context;

  @Test
  void getMessageFromFactoryBean() {
    Object message = context.getBean("message");
    assertThat(message).isInstanceOf(Message.class);
    assertThat(((Message)message).getText()).isEqualTo("Factory Bean");
  }
}
