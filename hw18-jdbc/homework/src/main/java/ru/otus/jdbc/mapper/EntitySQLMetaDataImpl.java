package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    EntityClassMetaData<?> entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData<?> entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }


    @Override
    public String getSelectAllSql() {
        return "select * from " + entityClassMetaData.getName().toLowerCase();
    }

    @Override
    public String getSelectByIdSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        List<Field> allFields = entityClassMetaData.getAllFields();
        for (int i = 0; i < allFields.size(); i++) {
            var field = allFields.get(i);
            sb.append(field.getName());
            if (i == allFields.size() - 1) {
                sb.append(" ");
            } else {
                sb.append(", ");
            }
        }
        sb.append("from ")
                .append(entityClassMetaData.getName().toLowerCase())
                .append(" where ")
                .append(entityClassMetaData.getIdField().getName())
                .append(" = ?");
        return sb.toString();
    }

    @Override
    public String getInsertSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("insert into ")
                .append(entityClassMetaData.getName().toLowerCase())
                .append("(");
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        for (int i = 0; i < fieldsWithoutId.size(); i++) {
            var field = fieldsWithoutId.get(i);
            sb.append(field.getName());
            if (i == fieldsWithoutId.size() - 1) {
                sb.append(")");
            } else {
                sb.append(", ");
            }
        }
        sb.append(" values (");
        for (int i = 0; i < fieldsWithoutId.size(); i++) {
            if (i == fieldsWithoutId.size() - 1) {
                sb.append("?)");
            } else {
                sb.append("?, ");
            }
        }
        return sb.toString();
    }

    @Override
    public String getUpdateSql() {
        StringBuilder sb = new StringBuilder();
        sb.append("update ")
                .append(entityClassMetaData.getName().toLowerCase())
                .append(" set (");
        List<Field> fieldsWithoutId = entityClassMetaData.getFieldsWithoutId();
        for (int i = 0; i < fieldsWithoutId.size(); i++) {
            var field = fieldsWithoutId.get(i);
            sb.append(field.getName());
            if (i == fieldsWithoutId.size() - 1) {
                sb.append(")");
            } else {
                sb.append(", ");
            }
        }
        sb.append(" = (");
        for (int i = 0; i < fieldsWithoutId.size(); i++) {
            if (i == fieldsWithoutId.size() - 1) {
                sb.append("?)");
            } else {
                sb.append("?, ");
            }
        }
        return sb.toString();
    }
}
