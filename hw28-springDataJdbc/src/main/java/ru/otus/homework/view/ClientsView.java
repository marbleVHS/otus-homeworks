package ru.otus.homework.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.homework.crm.model.Address;
import ru.otus.homework.crm.model.Client;
import ru.otus.homework.crm.model.ClientTemplateModel;
import ru.otus.homework.crm.model.Phone;
import ru.otus.homework.service.ClientService;

import java.util.List;
import java.util.Set;

@Controller
public class ClientsView {

    private final ClientService clientService;

    public ClientsView(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public String getClientsView(Model model) {
        List<ClientTemplateModel> allClients = clientService.getAllClients()
                .stream()
                .map(client -> new ClientTemplateModel(
                                client.getId(),
                                client.getName(),
                                client.getAddress().getStreet(),
                                client.getPhones()
                                        .stream()
                                        .findAny()
                                        .orElse(new Phone("no number"))
                                        .getNumber()
                        )
                ).toList();
        model.addAttribute("allClients", allClients);
        return "clients.html";
    }

    @PostMapping("/clients")
    public String postClient(
            @ModelAttribute("name") String clientName,
            @ModelAttribute("address") String clientAddress,
            @ModelAttribute("phone") String clientPhone
    ) {
        Client newClient = new Client(
                clientName,
                new Address(clientAddress),
                Set.of(new Phone(clientPhone))
        );
        clientService.createNewClient(newClient);
        return "redirect:/clients";
    }

}
