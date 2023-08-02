package ru.otus;

import com.google.common.base.Joiner;

@SuppressWarnings("java:S106")
public class HelloOtus {
    public static void main(String[] args) {
        Joiner joiner = Joiner.on(',');
        String shoppingList = joiner.join("Bread", "Carrots", "Apples", "Onions", "Vinegar");
        System.out.println(shoppingList);
    }
}
