package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

public class CacheDbServiceClient implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(CacheDbServiceClient.class);

    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;

    private final Map<String, Client> cache = new WeakHashMap<>();

    public CacheDbServiceClient(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            cache.put(client.getId().toString(), client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        Client cachedClient = cache.get(Long.toString(id));
        if (cachedClient == null) {
            return transactionRunner.doInTransaction(connection -> {
                var clientOptional = dataTemplate.findById(connection, id);
                log.info("client: {}", clientOptional);
                if (clientOptional.isPresent()) {
                    Client client = clientOptional.get();
                    cache.put(client.getId().toString(), client);
                }
                return clientOptional;
            });
        } else {
            return Optional.of(cachedClient);
        }
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            for (Client client : clientList) {
                cache.put(client.getId().toString(), client);
            }
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
