package homework.test;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

public class AnotherSampleTest {

    @Before
    public void setUp() {
        System.out.println(this.getClass().getName() +": before");
    }

    @Test
    public void checkSomething() {
        System.out.println(this.getClass().getName() +": test itself");
    }

    @After
    public void tearDown() {
        System.out.println(this.getClass().getName() +": after");
    }

}
