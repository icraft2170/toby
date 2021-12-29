package me.hyeonho.toby.learningtest.template;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

public class CalcSumTest {
    Calculator calculator;
    String numFilepath;

    @BeforeEach
    void setUp(){
        this.calculator = new Calculator();
        this.numFilepath = "/Users/sonhyeonho/study/Java/source/toby/src/test/java/me/hyeonho/toby/learningtest/template/numbers.txt";
    }

    @Test
    void sumOfNumbers() throws IOException{
        assertThat((int) calculator.calcSum(numFilepath)).isEqualTo(10);
    }

    @Test
    void multiplyOfNumbers() throws IOException{
        assertThat((int) calculator.calcMultiply(numFilepath)).isEqualTo(24);
    }


}
