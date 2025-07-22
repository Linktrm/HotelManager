package com.example.hotelmanager.model;

import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class HotelBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hotelbooking_seq")
    @SequenceGenerator(name = "hotelbooking_seq", sequenceName = "hotelbooking_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    private Hotel hotel;

    private String name;
    private String address;
    private LocalDate createdDate;

    @ManyToOne
    private Client client;

    public HotelBooking() {
        this.createdDate = LocalDate.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
