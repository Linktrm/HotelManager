package com.example.hotelmanager.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import com.example.hotelmanager.model.HotelBooking;
import com.example.hotelmanager.repository.HotelBookingRepository;

@Service
@ConditionalOnProperty(name = "app.data-type", havingValue = "DB")
public class HotelBookingDbService implements HotelBookingService {

    @Autowired
    private HotelBookingRepository repository;

    @Override
    public List<HotelBooking> findAll() {
        return repository.findAll();
    }

    @Override
    public HotelBooking save(HotelBooking entity) {
        entity.setName(entity.getHotel().getName());
        entity.setAddress(entity.getHotel().getAddress());
        return repository.save(entity);
    }

    @Override
    public HotelBooking update(Long id, HotelBooking entity) {
        entity.setId(id);
        return repository.save(entity);
    }

    @Override
    public HotelBooking findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
