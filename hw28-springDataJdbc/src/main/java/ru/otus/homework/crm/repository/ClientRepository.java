package ru.otus.homework.crm.repository;

import org.springframework.data.repository.ListCrudRepository;
import ru.otus.homework.crm.model.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {
}
