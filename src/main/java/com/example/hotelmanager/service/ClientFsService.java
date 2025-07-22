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
import com.example.hotelmanager.model.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.annotation.PostConstruct;

@Service
@ConditionalOnProperty(name = "app.data-type", havingValue = "FS")
public class ClientFsService implements ClientService {

    @Value("${app.fs-folder}")
    private String fsFolder;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Path clientFolder;
    private Path metadataFile;
    private int lastIndex = 0;
    private int totalRegistries = 0;

    @PostConstruct
    public void init() {
        try {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            clientFolder = Paths.get(fsFolder, "Client");
            Files.createDirectories(clientFolder);
            metadataFile = clientFolder.resolve("_metadata.json");

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
    public List<Client> findAll() {
        List<Client> list = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(clientFolder, "*.json")) {
            for (Path file : stream) {
                if (!file.getFileName().toString().equals("_metadata.json")) {
                    list.add(objectMapper.readValue(file.toFile(), Client.class));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public Client save(Client entity) {
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

            Path file = clientFolder.resolve(entity.getId() + ".json");
            objectMapper.writeValue(file.toFile(), entity);
            return entity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Client update(Long id, Client entity) {
        try {
            Path file = clientFolder.resolve(id + ".json");
            if (!Files.exists(file)) {
                throw new RuntimeException("Cliente con ID " + id + " no existe");
            }

            entity.setId(id);
            objectMapper.writeValue(file.toFile(), entity);
            return entity;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Client findById(Long id) {
        return findAll().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void deleteById(Long id) {
        try {
            Path file = clientFolder.resolve(id + ".json");
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
