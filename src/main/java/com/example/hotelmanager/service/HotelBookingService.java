package com.example.hotelmanager.service;

import java.util.List;
import com.example.hotelmanager.model.HotelBooking;

public interface HotelBookingService {
    List<HotelBooking> findAll();

    HotelBooking findById(Long id);

    HotelBooking save(HotelBooking client);

    HotelBooking update(Long id, HotelBooking entity);

    void deleteById(Long id);
}
