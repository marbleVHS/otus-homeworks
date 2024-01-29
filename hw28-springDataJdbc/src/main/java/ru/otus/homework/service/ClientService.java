package ru.otus.homework.service;

import org.springframework.stereotype.Service;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.repository.ClientRepository;
import ru.otus.homework.sessionmanager.TransactionManager;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final TransactionManager transactionManager;

    public ClientService(
            ClientRepository clientRepository,
            TransactionManager transactionManager
    ) {
        this.clientRepository = clientRepository;
        this.transactionManager = transactionManager;
    }

    public Optional<Client> getClientById(Long id) {
        return transactionManager.doInTransaction(() -> clientRepository.findById(id));
    }

    public List<Client> getAllClients() {
        return transactionManager.doInTransaction(clientRepository::findAll);
    }

    public Client createNewClient(Client newClient) {
        return transactionManager.doInTransaction(() -> clientRepository.save(newClient));
    }
}
