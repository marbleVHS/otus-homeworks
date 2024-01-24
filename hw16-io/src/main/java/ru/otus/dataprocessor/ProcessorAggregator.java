package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        // группирует выходящий список по name, при этом суммирует поля value
        SortedMap<String, Double> sumsMap = new TreeMap<>(String::compareTo);
        for (Measurement measurement : data) {
            sumsMap.put(
                    measurement.name(),
                    sumsMap.getOrDefault(measurement.name(), 0.0) + measurement.value()
            );
        }
        return sumsMap;
    }
}
