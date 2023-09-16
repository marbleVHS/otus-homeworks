package homework;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SuppressWarnings({"java:S106", "java:S1854", "java:S3011"})
public class TestLauncher {

    public void performTesting(String... testClassesNames) {
        Optional<TestRunResult> finalResultOptional = Arrays.stream(testClassesNames)
                .map(testClassName -> {
                    try {
                        return performTestingForClass(testClassName);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .reduce((testRunResult, testRunResult2) -> {
                    int successCount = testRunResult.successTestsCount() + testRunResult2.successTestsCount();
                    int failCount = testRunResult.failedTestsCount() + testRunResult2.failedTestsCount();
                    return new TestRunResult(successCount, failCount);
                });
        finalResultOptional.ifPresent(this::printOutResults);
    }

    private TestRunResult performTestingForClass(String testClassName) throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> testClass = Class.forName(testClassName);
        Constructor<?> testClassConstructor = testClass.getConstructor();
        testClassConstructor.setAccessible(true);
        Method[] methods = testClass.getMethods();
        List<Method> beforeMethods = findBeforeMethods(methods);
        List<Method> afterMethods = findAfterMethods(methods);
        List<Method> testMethods = findTestMethods(methods);
        int successTestsCount = 0;
        int failedTestsCount = 0;
        for (Method testMethod : testMethods) {
            Object testClassInstance = testClassConstructor.newInstance();
            try {
                for (Method beforeMethod : beforeMethods) {
                    beforeMethod.invoke(testClassInstance);
                }
                testMethod.invoke(testClassInstance);
                ++successTestsCount;
            } catch (Exception e) {
                ++failedTestsCount;
            } finally {
                for (Method afterMethod : afterMethods) {
                    try {
                        afterMethod.invoke(testClassInstance);
                    } catch (Exception e) {

                    }
                }
            }
        }
        return new TestRunResult(successTestsCount, failedTestsCount);
    }

    private List<Method> findTestMethods(Method[] methods) {
        List<Method> testMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
        }
        return testMethods;
    }

    private List<Method> findBeforeMethods(Method[] methods) {
        List<Method> beforeMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                beforeMethods.add(method);
            }
        }
        return beforeMethods;
    }

    private List<Method> findAfterMethods(Method[] methods) {
        List<Method> afterMethods = new ArrayList<>();
        for (Method method : methods) {
            if (method.isAnnotationPresent(After.class)) {
                afterMethods.add(method);
            }
        }
        return afterMethods;
    }

    private void printOutResults(TestRunResult trr) {
        System.out.printf("""
                        Failed tests: %d
                        Successful tests: %d
                        Total tests: %d
                        """,
                trr.failedTestsCount(),
                trr.successTestsCount(),
                trr.successTestsCount() + trr.failedTestsCount()
        );
    }
}
