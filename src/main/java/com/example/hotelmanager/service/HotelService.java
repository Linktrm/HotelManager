package com.example.hotelmanager.service;

import java.util.List;
import com.example.hotelmanager.model.Hotel;

public interface HotelService {
    List<Hotel> findAll();

    Hotel findById(Long id);

    Hotel save(Hotel hotel);

    Hotel update(Long id, Hotel entity);

    void deleteById(Long id);
}
