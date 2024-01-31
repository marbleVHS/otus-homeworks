package ru.otus.server.service;


import java.util.ArrayList;
import java.util.List;

public class SequenceGeneratorServiceImpl implements SequenceGeneratorService {

    @Override
    public List<Integer> getSequenceMembers(int from, int to) {
        if (to < from) {
            throw new IllegalArgumentException("final value of the sequence can't be less than initial value");
        }
        List<Integer> sequence = new ArrayList<>(to - from);
        for (int i = from; i <= to; i++) {
            sequence.add(i);
        }
        return sequence;
    }
}
