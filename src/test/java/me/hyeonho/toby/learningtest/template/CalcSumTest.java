package me.hyeonho.toby.learningtest.template;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

public class CalcSumTest {
    @Test
    void sumOfNumbers() throws IOException{
        Calculator calculator = new Calculator();
        int sum = calculator.calcSum("/Users/sonhyeonho/study/Java/source/toby/src/test/java/me/hyeonho/toby/learningtest/template/numbers.txt");
        assertThat(sum).isEqualTo(10);
    }
}
