package com.example.hotelmanager.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import com.example.hotelmanager.model.Hotel;
import com.example.hotelmanager.repository.HotelRepository;

@Service
@ConditionalOnProperty(name = "app.data-type", havingValue = "DB")
public class HotelDbService implements HotelService {

    @Autowired
    private HotelRepository repository;

    @Override
    public List<Hotel> findAll() {
        return repository.findAll();
    }

    @Override
    public Hotel save(Hotel entity) {
        return repository.save(entity);
    }

    @Override
    public Hotel update(Long id, Hotel entity) {
        entity.setId(id);
        return repository.save(entity);
    }

    @Override
    public Hotel findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
