package com.example.hotelmanager.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import com.example.hotelmanager.model.Client;
import com.example.hotelmanager.repository.ClientRepository;

@Service
@ConditionalOnProperty(name = "app.data-type", havingValue = "DB")
public class ClientDbService implements ClientService {

    @Autowired
    private ClientRepository repository;

    @Override
    public List<Client> findAll() {
        return repository.findAll();
    }

    @Override
    public Client save(Client entity) {
        return repository.save(entity);
    }

    @Override
    public Client update(Long id, Client entity) {
        entity.setId(id);
        return repository.save(entity);
    }

    @Override
    public Client findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
