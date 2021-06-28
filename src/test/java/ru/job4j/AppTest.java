package ru.job4j;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AppTest {

    @Test
    public void test() {
        assertThat(new App().calc(1, 1), is(2));
    }

    @Test
    public void test2() {
        assertThat(new App().calc(1, 3), is(4));
    }
}
