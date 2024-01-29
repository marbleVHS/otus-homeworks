package ru.otus.homework.crm.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "phone")
@Getter
public class Phone {

    @Id
    @Column("id")
    private final Long id;

    @Column("number")
    private final String number;

    public Phone(String number) {
        this.id = null;
        this.number = number;
    }

    @PersistenceCreator
    public Phone(Long id, String number) {
        this.id = id;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Phone{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }

}
