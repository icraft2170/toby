package me.hyeonho.toby.learningtest.template;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
