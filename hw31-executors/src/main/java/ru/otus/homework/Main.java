package ru.otus.homework;

import java.util.ArrayDeque;
import java.util.Deque;

public class Main {
    private String last = "Thread2";


    public static void main(String[] args) {
        Main monitor = new Main();
        Deque<Integer> numbers = new ArrayDeque<>();
        for (int i = 1; i <= 10; i++) {
            numbers.add(i);
        }
        for (int i = 9; i >= 1; i--) {
            numbers.add(i);
        }
        new Thread(() -> monitor.counting("Thread1", new ArrayDeque<>(numbers))).start();
        new Thread(() -> monitor.counting("Thread2", new ArrayDeque<>(numbers))).start();
    }

    private synchronized void counting(String threadName, Deque<Integer> numbers) {
        while (!numbers.isEmpty() && !Thread.currentThread().isInterrupted()) {
            try {
                while (last.equals(threadName)) {
                    this.wait();
                }

                System.out.println(threadName + ": " + numbers.pop());
                last = threadName;
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
