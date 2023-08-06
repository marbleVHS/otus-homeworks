package homework;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class CustomerService {

    private final NavigableMap<Customer, String> customerMap = new TreeMap<>();


    public Map.Entry<Customer, String> getSmallest() {
        return getMapEntryDeepCopy(customerMap.firstEntry());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getMapEntryDeepCopy(customerMap.higherEntry(customer));
    }

    public void add(Customer customer, String data) {
        customerMap.put(customer, data);
    }

    private Map.Entry<Customer, String> getMapEntryDeepCopy(Map.Entry<Customer, String> entry) {
        if(entry == null) {
            return null;
        }
        Customer customer = entry.getKey();
        String value = entry.getValue();
        Customer customerCopy = new Customer(customer.getId(), customer.getName(), customer.getScores());
        return Map.entry(customerCopy, value);
    }
}
