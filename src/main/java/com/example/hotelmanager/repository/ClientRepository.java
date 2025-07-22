package com.example.hotelmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hotelmanager.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
