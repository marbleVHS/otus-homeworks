package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class ResourcesFileLoader implements Loader {
    private static final TypeReference<List<Measurement>> TYPE_REFERENCE = new TypeReference<>() {
    };

    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        // читает файл, парсит и возвращает результат
        try {
            URL resource = this.getClass().getResource("/" + fileName);
            return new ObjectMapper().readValue(resource, TYPE_REFERENCE);
        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
