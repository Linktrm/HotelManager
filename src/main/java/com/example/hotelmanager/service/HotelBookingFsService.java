package com.example.hotelmanager.service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import com.example.hotelmanager.model.HotelBooking;
import com.example.hotelmanager.model.dto.HotelBookingDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;

@Service
@ConditionalOnProperty(name = "app.data-type", havingValue = "FS")
public class HotelBookingFsService implements HotelBookingService {

    @Value("${app.fs-folder}")
    private String fsFolder;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Path hotelBookingFolder;
    private Path metadataFile;
    private int lastIndex = 0;
    private int totalRegistries = 0;

    private final HotelService hotelService;
    private final ClientService clientService;

    public HotelBookingFsService(
            HotelService hotelService,
            ClientService clientService) {
        this.hotelService = hotelService;
        this.clientService = clientService;
    }

    @PostConstruct
    public void init() {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            hotelBookingFolder = Paths.get(fsFolder, "HotelBooking");
            Files.createDirectories(hotelBookingFolder);
            metadataFile = hotelBookingFolder.resolve("_metadata.json");

            if (Files.exists(metadataFile)) {
                Map<String, Integer> meta = objectMapper.readValue(metadataFile.toFile(), Map.class);
                lastIndex = meta.getOrDefault("LAST_INDEX", 0);
                totalRegistries = meta.getOrDefault("TOTAL_REGISTRIES", 0);
            } else {
                saveMetadata();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveMetadata() throws IOException {
        Map<String, Object> meta = new HashMap<>();
        meta.put("LAST_INDEX", lastIndex);
        meta.put("TOTAL_REGISTRIES", totalRegistries);
        objectMapper.writeValue(metadataFile.toFile(), meta);
    }

    @Override
    public List<HotelBooking> findAll() {
        List<HotelBooking> list = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(hotelBookingFolder, "*.json")) {
            for (Path file : stream) {
                if (!file.getFileName().toString().equals("_metadata.json")) {
                    HotelBookingDTO dto = objectMapper.readValue(file.toFile(), HotelBookingDTO.class);
                    HotelBooking booking = fromDTO(dto);
                    list.add(booking);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    private HotelBookingDTO toDTO(HotelBooking booking) {
        HotelBookingDTO dto = new HotelBookingDTO();
        dto.setId(booking.getId());
        dto.setName(booking.getHotel() != null ? booking.getHotel().getName() : null);
        dto.setAddress(booking.getHotel() != null ? booking.getHotel().getAddress() : null);
        dto.setCreatedDate(booking.getCreatedDate());
        dto.setHotelId(booking.getHotel() != null ? booking.getHotel().getId() : null);
        dto.setClientId(booking.getClient() != null ? booking.getClient().getId() : null);
        return dto;
    }

    private HotelBooking fromDTO(HotelBookingDTO dto) {
        HotelBooking booking = new HotelBooking();
        booking.setId(dto.getId());
        booking.setName(dto.getName());
        booking.setAddress(dto.getAddress());
        booking.setCreatedDate(dto.getCreatedDate());

        if (dto.getHotelId() != null) {
            booking.setHotel(hotelService.findById(dto.getHotelId()));
        }
        if (dto.getClientId() != null) {
            booking.setClient(clientService.findById(dto.getClientId()));
        }
        return booking;
    }

    @Override
    public HotelBooking save(HotelBooking entity) {
        try {
            if (entity.getId() == null) {
                lastIndex++;
                totalRegistries++;
                entity.setId((long) lastIndex);
                saveMetadata();
            }

            if (entity.getCreatedDate() == null) {
                entity.setCreatedDate(LocalDate.now());
            }

            HotelBookingDTO dto = toDTO(entity);

            entity.setName(entity.getHotel().getName());
            entity.setAddress(entity.getHotel().getAddress());

            Path file = hotelBookingFolder.resolve(entity.getId() + ".json");
            objectMapper.writeValue(file.toFile(), dto);
            return entity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HotelBooking update(Long id, HotelBooking entity) {
        try {
            Path file = hotelBookingFolder.resolve(id + ".json");
            if (!Files.exists(file)) {
                throw new RuntimeException("Reserva con ID " + id + " no existe");
            }

            entity.setId(id);
            objectMapper.writeValue(file.toFile(), entity);
            return entity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HotelBooking findById(Long id) {
        return findAll().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        try {
            Path file = hotelBookingFolder.resolve(id + ".json");
            if (Files.exists(file)) {
                Files.delete(file);
                totalRegistries--;
                saveMetadata();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
