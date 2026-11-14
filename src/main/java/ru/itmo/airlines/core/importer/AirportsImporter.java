package ru.itmo.airlines.core.importer;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import ru.itmo.airlines.core.dao.entity.AirportEntity;
import ru.itmo.airlines.core.service.AirportService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class AirportsImporter implements CommandLineRunner {
    private final AirportService airportService;

    public void importAirports() throws IOException {
        var resource = new ClassPathResource("data/airports.csv");
        try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] fields = line.split(",");
                AirportEntity airport = new AirportEntity();
                airport.setIataCode(fields[0]);
                airport.setName(fields[1]);
                airport.setCity(fields[2]);
                airport.setCountry(fields[3]);
                airport.setLatitude(new BigDecimal(fields[4]));
                airport.setLongitude(new BigDecimal(fields[5]));
                airport.setTimezone(fields[6]);
                airportService.saveAirport(airport);
            }
        }
    }

    @Override
    public void run(String... args) throws IOException {
        importAirports();
    }
}
