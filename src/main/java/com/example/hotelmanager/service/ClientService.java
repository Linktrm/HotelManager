package com.example.hotelmanager.service;

import java.util.List;
import com.example.hotelmanager.model.Client;

public interface ClientService {
    List<Client> findAll();

    Client findById(Long id);

    Client save(Client client);

    Client update(Long id, Client entity);

    void deleteById(Long id);
}
