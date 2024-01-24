package ru.otus.jdbc.mapper;

import ru.otus.crm.annotation.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> type;

    public EntityClassMetaDataImpl(Class<T> type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        try {
            Class<?>[] fieldsTypes = getAllFields()
                    .stream()
                    .map(Field::getType)
                    .toArray(Class<?>[]::new);
            return type.getConstructor(fieldsTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Field getIdField() {
        Field[] declaredFields = type.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getAnnotation(Id.class) != null) {
                return declaredField;
            }
        }
        return null;
    }

    @Override
    public List<Field> getAllFields() {
        return Arrays.asList(type.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        List<Field> result = new ArrayList<>();
        Field[] declaredFields = type.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.getAnnotation(Id.class) == null) {
                result.add(declaredField);
            }
        }
        return result;
    }

}
