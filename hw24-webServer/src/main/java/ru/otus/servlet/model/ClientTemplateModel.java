package ru.otus.servlet.model;

import lombok.Getter;
import ru.otus.crm.model.Client;

@Getter
public class ClientTemplateModel {

    private final Long id;
    private final String name;
    private final String address;
    private final String phoneNumber;

    public ClientTemplateModel(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.address = client.getAddress().getStreet();
        this.phoneNumber = client.getPhones().get(0).getNumber();
    }
}
