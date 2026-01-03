package com.renault.garage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.dto.VehicleDto;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.FuelType;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class VehicleIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private GarageRepository garageRepository;

        @Autowired
        private VehicleRepository vehicleRepository;

        private Garage testGarage;
        private VehicleDto testVehicleDto;

        @BeforeEach
        void setUp() {
                vehicleRepository.deleteAll();
                garageRepository.deleteAll();

                testGarage = new Garage("Garage Test", "1 Rue Test", "0123456789",
                                "test@renault.fr");
                testGarage = garageRepository.save(testGarage);

                testVehicleDto = new VehicleDto();
                testVehicleDto.setBrand("Renault");
                testVehicleDto.setModel("Clio");
                testVehicleDto.setManufacturingYear(2023);
                testVehicleDto.setFuelType(FuelType.ESSENCE);
        }

        @Test
        @DisplayName("POST /api/garages/{id}/vehicles - Ajouter un véhicule avec succès")
        void addVehicle_Success() throws Exception {
                mockMvc.perform(post("/api/garages/{garageId}/vehicles", testGarage.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testVehicleDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.brand").value("Renault"))
                                .andExpect(jsonPath("$.model").value("Clio"))
                                .andExpect(jsonPath("$.garageId").value(testGarage.getId()));
        }

        @Test
        @DisplayName("POST /api/garages/{id}/vehicles - Garage non trouvé")
        void addVehicle_GarageNotFound() throws Exception {
                mockMvc.perform(post("/api/garages/{garageId}/vehicles", 9999)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testVehicleDto)))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("POST /api/garages/{id}/vehicles - Validation échouée")
        void addVehicle_ValidationFailed() throws Exception {
                testVehicleDto.setBrand(null);

                mockMvc.perform(post("/api/garages/{garageId}/vehicles", testGarage.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testVehicleDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errors.brand").exists());
        }

        @Test
        @DisplayName("GET /api/garages/{id}/vehicles - Lister les véhicules d'un garage")
        void getVehiclesByGarage_Success() throws Exception {
                Vehicle vehicle1 = new Vehicle("Renault", "Clio", 2023, FuelType.ESSENCE);
                vehicle1.setGarage(testGarage);
                vehicleRepository.save(vehicle1);

                Vehicle vehicle2 = new Vehicle("Renault", "Megane", 2022, FuelType.DIESEL);
                vehicle2.setGarage(testGarage);
                vehicleRepository.save(vehicle2);

                mockMvc.perform(get("/api/garages/{garageId}/vehicles", testGarage.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[*].brand", everyItem(equalTo("Renault"))));
        }

        @Test
        @DisplayName("GET /api/vehicles/model/{model} - Lister par modèle")
        void getVehiclesByModel_Success() throws Exception {
                Garage garage2 = garageRepository.save(
                                new Garage("Garage 2", "Addr 2", "0987654321", "g2@test.fr"));

                Vehicle v1 = new Vehicle("Renault", "Clio", 2023, FuelType.ESSENCE);
                v1.setGarage(testGarage);
                vehicleRepository.save(v1);

                Vehicle v2 = new Vehicle("Renault", "Clio", 2022, FuelType.DIESEL);
                v2.setGarage(garage2);
                vehicleRepository.save(v2);

                Vehicle v3 = new Vehicle("Renault", "Megane", 2023, FuelType.ELECTRIC);
                v3.setGarage(testGarage);
                vehicleRepository.save(v3);

                mockMvc.perform(get("/api/vehicles/model/{model}", "Clio"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[*].model", everyItem(equalTo("Clio"))));
        }

        @Test
        @DisplayName("GET /api/vehicles/fuel-type/{fuelType} - Lister par type de carburant")
        void getVehiclesByFuelType_Success() throws Exception {
                Vehicle v1 = new Vehicle("Renault", "ZOE", 2023, FuelType.ELECTRIC);
                v1.setGarage(testGarage);
                vehicleRepository.save(v1);

                Vehicle v2 = new Vehicle("Renault", "Megane E-Tech", 2023, FuelType.ELECTRIC);
                v2.setGarage(testGarage);
                vehicleRepository.save(v2);

                mockMvc.perform(get("/api/vehicles/fuel-type/{fuelType}", "ELECTRIC"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$", hasSize(2)))
                                .andExpect(jsonPath("$[*].fuelType", everyItem(equalTo("ELECTRIC"))));
        }

        @Test
        @DisplayName("PUT /api/vehicles/{id} - Modifier un véhicule")
        void updateVehicle_Success() throws Exception {
                Vehicle vehicle = new Vehicle("Renault", "Clio", 2020, FuelType.ESSENCE);
                vehicle.setGarage(testGarage);
                vehicle = vehicleRepository.save(vehicle);

                VehicleDto updateDto = new VehicleDto();
                updateDto.setBrand("Renault");
                updateDto.setModel("Clio RS");
                updateDto.setManufacturingYear(2023);
                updateDto.setFuelType(FuelType.ESSENCE);

                mockMvc.perform(put("/api/vehicles/{id}", vehicle.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.model").value("Clio RS"))
                                .andExpect(jsonPath("$.manufacturingYear").value(2023));
        }

        @Test
        @DisplayName("DELETE /api/vehicles/{id} - Supprimer un véhicule")
        void deleteVehicle_Success() throws Exception {
                Vehicle vehicle = new Vehicle("Renault", "Twingo", 2020, FuelType.ESSENCE);
                vehicle.setGarage(testGarage);
                vehicle = vehicleRepository.save(vehicle);

                mockMvc.perform(delete("/api/vehicles/{id}", vehicle.getId()))
                                .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("POST /api/vehicles/{id}/transfer/{targetId} - Transférer un véhicule")
        void transferVehicle_Success() throws Exception {
                Garage targetGarage = garageRepository.save(
                                new Garage("Garage Target", "Target Addr", "0555555555", "target@test.fr"));

                Vehicle vehicle = new Vehicle("Renault", "Captur", 2023, FuelType.HYBRID);
                vehicle.setGarage(testGarage);
                vehicle = vehicleRepository.save(vehicle);

                mockMvc.perform(post("/api/vehicles/{vehicleId}/transfer/{targetGarageId}",
                                vehicle.getId(), targetGarage.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.garageId").value(targetGarage.getId()));
        }
}
