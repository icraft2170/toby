package me.hyeonho.toby.learningtest.dynmaicProxy;

import org.springframework.beans.factory.FactoryBean;

public class MessageFactoryBean implements FactoryBean<Message> {
    private String text;

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
