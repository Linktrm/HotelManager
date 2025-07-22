package com.example.hotelmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hotelmanager.model.HotelBooking;

public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {
}
