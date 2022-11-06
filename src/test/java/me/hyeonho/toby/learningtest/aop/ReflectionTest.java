package me.hyeonho.toby.learningtest.aop;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.junit.jupiter.api.Test;

public class ReflectionTest {

  @Test
  void invokeMethod() throws Exception{
    String name = "Spring";

    assertThat(name.length()).isEqualTo(6);

    Method lengthMethod = String.class.getMethod("length");
    assertThat((Integer) lengthMethod.invoke(name)).isEqualTo(6);


    assertThat(name.charAt(0)).isEqualTo('S');

    Method charAtMethod = String.class.getMethod("charAt", int.class);
    assertThat(charAtMethod.invoke(name, 0)).isEqualTo('S');
  }

  @Test
  void simpleProxy() {
    Hello hello = new HelloTarget();
    assertThat(hello.sayHello("Toby")).isEqualTo("Hello Toby");
    assertThat(hello.sayHi("Toby")).isEqualTo("Hi Toby");
    assertThat(hello.sayThankYou("Toby")).isEqualTo("Thank You Toby");

    Hello helloUppercase = new HelloUppercase(new HelloTarget());
    assertThat(helloUppercase.sayHello("Toby")).isEqualTo("HELLO TOBY");
    assertThat(helloUppercase.sayHi("Toby")).isEqualTo("HI TOBY");
    assertThat(helloUppercase.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");

    Hello dynamicProxyHello = (Hello) Proxy.newProxyInstance(
        getClass().getClassLoader(), //ClassLoader 설정
        new Class[]{Hello.class}, // TypeToken 설정
        new UppercaseHandler(new HelloTarget()) // Handler 설정
    );
    assertThat(dynamicProxyHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
    assertThat(dynamicProxyHello.sayHi("Toby")).isEqualTo("HI TOBY");
    assertThat(dynamicProxyHello.sayThankYou("Toby")).isEqualTo("THANK YOU TOBY");
  }
}
