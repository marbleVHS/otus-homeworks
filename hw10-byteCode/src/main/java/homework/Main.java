package homework;

import homework.calculation.TestLogging;

public class Main {

    public static void main(String[] args) {
        TestLogging testLogging = new TestLogging();

        testLogging.calculation(6);
        testLogging.calculation(6, 5);
        testLogging.calculation(6, 5, "four");
        testLogging.calculation(new Object(), 6, 5, "four", "7");
    }

}
