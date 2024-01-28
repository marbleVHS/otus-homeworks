package ru.otus.homework.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.model.ErrorMessage;
import ru.otus.homework.service.ClientService;

import java.util.Optional;

@RestController
public class ClientResource {

    private final ClientService clientService;

    public ClientResource(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/api/clients/{id}")
    public Object getClientById(@PathVariable("id") Long id){
        Optional<Client> clientById = clientService.getClientById(id);
        if(clientById.isPresent()) {
            return clientById.get();
        } else {
            return new ErrorMessage("Couldn't find client with id: " + id);
        }
    }

}
