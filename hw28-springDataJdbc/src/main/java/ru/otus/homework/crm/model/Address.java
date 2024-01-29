package ru.otus.homework.crm.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name = "address")
public class Address {

    @Id
    @Column("id")
    private Long id;

    @Column("street")
    private final String street;

    public Address(String street) {
        this.id = null;
        this.street = street;
    }

    @PersistenceCreator
    public Address(Long id, String street) {
        this.id = id;
        this.street = street;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                '}';
    }
}
