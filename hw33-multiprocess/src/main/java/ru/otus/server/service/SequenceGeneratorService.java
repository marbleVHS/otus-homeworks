package ru.otus.server.service;

import java.util.List;

public interface SequenceGeneratorService {

    List<Integer> getSequenceMembers(int from, int to);

}
