package me.hyeonho.toby.learningtest.aop.factory_bean;

import org.springframework.beans.factory.FactoryBean;


public class MessageFactoryBean implements FactoryBean<Message> {
  private final String text;

  public MessageFactoryBean(String text) {
    this.text = text;
  }

  @Override
  public boolean isSingleton() {
    return false;
  }

  @Override
  public Message getObject() throws Exception {
    return Message.newMessage(this.text);
  }

  @Override
  public Class<?> getObjectType() {
    return Message.class;
  }
}
