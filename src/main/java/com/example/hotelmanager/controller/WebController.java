package com.example.hotelmanager.controller;

import java.beans.PropertyEditorSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.hotelmanager.model.Client;
import com.example.hotelmanager.model.Hotel;
import com.example.hotelmanager.model.HotelBooking;
import com.example.hotelmanager.service.ClientService;
import com.example.hotelmanager.service.HotelBookingService;
import com.example.hotelmanager.service.HotelService;

@Controller
public class WebController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private HotelBookingService bookingService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // ---------- CLIENTES ----------
    @GetMapping("/clients")
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("client", new Client());
        return "clients";
    }

    @PostMapping("/clients")
    public String saveClient(@ModelAttribute Client client) {
        clientService.save(client);
        return "redirect:/clients";
    }

    @GetMapping("/clients/edit/{id}")
    public String editClient(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.findById(id));
        model.addAttribute("clients", clientService.findAll());
        return "clients";
    }

    @GetMapping("/clients/delete/{id}")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteById(id);
        return "redirect:/clients";
    }

    // ---------- HOTELES ----------
    @GetMapping("/hotels")
    public String listHotels(Model model) {
        model.addAttribute("hotels", hotelService.findAll());
        model.addAttribute("hotel", new Hotel());
        return "hotels";
    }

    @PostMapping("/hotels")
    public String saveHotel(@ModelAttribute Hotel hotel) {
        hotelService.save(hotel);
        return "redirect:/hotels";
    }

    @GetMapping("/hotels/edit/{id}")
    public String editHotel(@PathVariable Long id, Model model) {
        model.addAttribute("hotel", hotelService.findById(id));
        model.addAttribute("hotels", hotelService.findAll());
        return "hotels";
    }

    @GetMapping("/hotels/delete/{id}")
    public String deleteHotel(@PathVariable Long id) {
        hotelService.deleteById(id);
        return "redirect:/hotels";
    }

    // ---------- RESERVAS ----------
    @GetMapping("/bookings")
    public String listBookings(Model model) {
        model.addAttribute("bookings", bookingService.findAll());
        model.addAttribute("booking", new HotelBooking());
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("hotels", hotelService.findAll());
        return "bookings";
    }

    @PostMapping("/bookings")
    public String saveBooking(@ModelAttribute HotelBooking booking) {
        bookingService.save(booking);
        return "redirect:/bookings";
    }

    @GetMapping("/bookings/edit/{id}")
    public String editBooking(@PathVariable Long id, Model model) {
        model.addAttribute("booking", bookingService.findById(id));
        model.addAttribute("bookings", bookingService.findAll());
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("hotels", hotelService.findAll());
        return "bookings";
    }

    @GetMapping("/bookings/delete/{id}")
    public String deleteBooking(@PathVariable Long id) {
        bookingService.deleteById(id);
        return "redirect:/bookings";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Hotel.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Long id = Long.valueOf(text);
                setValue(hotelService.findById(id));
            }
        });

        binder.registerCustomEditor(Client.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Long id = Long.valueOf(text);
                setValue(clientService.findById(id));
            }
        });
    }
}
