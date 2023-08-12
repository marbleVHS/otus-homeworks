package homework;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomerReverseOrder {

    private final Deque<Customer> customersDeque = new ArrayDeque<>();

    public void add(Customer customer) {
        customersDeque.add(customer);
    }

    public Customer take() {
        return customersDeque.pollLast();
    }
}
