package com.example.hotelmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hotelmanager.model.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
