package homework.test;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

public class SampleTest {

    @Before
    public void setUp(){
        System.out.println(this.getClass().getName() +": before");
    }

    @After
    public void tearDown() {
        System.out.println(this.getClass().getName() +": after");
    }

    @Test
    public void checkRemoveChicken() {
        System.out.println(this.getClass().getName() +": test that doesn't fail");
    }

    @Test
    public void checkAddChicken() {
        System.out.println(this.getClass().getName() +": test that is going to fail");
        throw new AssertionError("test have failed");
    }


}
