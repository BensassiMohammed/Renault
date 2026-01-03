package com.renault.garage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renault.garage.dto.GarageDto;
import com.renault.garage.dto.OpeningTimeDto;
import com.renault.garage.entity.Garage;
import com.renault.garage.repository.GarageRepository;
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

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class GarageIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private GarageRepository garageRepository;

        private GarageDto testGarageDto;

        @BeforeEach
        void setUp() {
                garageRepository.deleteAll();

                testGarageDto = new GarageDto();
                testGarageDto.setName("Garage Renault Test");
                testGarageDto.setAddress("1 Rue du Test");
                testGarageDto.setCity("Paris");
                testGarageDto.setTelephone("0123456789");
                testGarageDto.setEmail("test@renault.fr");

                // Horaires d'ouverture
                OpeningTimeDto morningSlot = new OpeningTimeDto(LocalTime.of(8, 0), LocalTime.of(12, 0));
                OpeningTimeDto afternoonSlot = new OpeningTimeDto(LocalTime.of(14, 0), LocalTime.of(18, 0));
                testGarageDto.setOpeningHours(Map.of(
                                DayOfWeek.MONDAY, List.of(morningSlot, afternoonSlot),
                                DayOfWeek.TUESDAY, List.of(morningSlot, afternoonSlot)));
        }

        @Test
        @DisplayName("POST /api/garages - Créer un garage avec succès")
        void createGarage_Success() throws Exception {
                mockMvc.perform(post("/api/garages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testGarageDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.name").value("Garage Renault Test"))
                                .andExpect(jsonPath("$.email").value("test@renault.fr"));
        }

        @Test
        @DisplayName("POST /api/garages - Validation échouée (email manquant)")
        void createGarage_ValidationFailed() throws Exception {
                testGarageDto.setEmail(null);

                mockMvc.perform(post("/api/garages")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(testGarageDto)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.errors.email").exists());
        }

        @Test
        @DisplayName("GET /api/garages/{id} - Récupérer un garage existant")
        void getGarageById_Success() throws Exception {
                Garage savedGarage = garageRepository.save(
                                new Garage("Garage Test", "123 Test", "0123456789", "test@test.fr"));

                mockMvc.perform(get("/api/garages/{id}", savedGarage.getId()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(savedGarage.getId()))
                                .andExpect(jsonPath("$.name").value("Garage Test"));
        }

        @Test
        @DisplayName("GET /api/garages/{id} - Garage non trouvé")
        void getGarageById_NotFound() throws Exception {
                mockMvc.perform(get("/api/garages/{id}", 9999))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.message").value(containsString("9999")));
        }

        @Test
        @DisplayName("GET /api/garages - Liste paginée")
        void getAllGarages_Paginated() throws Exception {
                garageRepository.save(new Garage("Garage A", "Addr A", "0111111111", "a@test.fr"));
                garageRepository.save(new Garage("Garage B", "Addr B", "0222222222", "b@test.fr"));
                garageRepository.save(new Garage("Garage C", "Addr C", "0333333333", "c@test.fr"));

                mockMvc.perform(get("/api/garages")
                                .param("page", "0")
                                .param("size", "2")
                                .param("sort", "name,asc"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content", hasSize(2)))
                                .andExpect(jsonPath("$.totalElements").value(3))
                                .andExpect(jsonPath("$.content[0].name").value("Garage A"));
        }

        @Test
        @DisplayName("PUT /api/garages/{id} - Modifier un garage")
        void updateGarage_Success() throws Exception {
                Garage savedGarage = garageRepository.save(
                                new Garage("Garage Old", "Old Addr", "0123456789", "old@test.fr"));

                GarageDto updateDto = new GarageDto();
                updateDto.setName("Garage Updated");
                updateDto.setAddress("New Addr");
                updateDto.setCity("Lyon");
                updateDto.setTelephone("0987654321");
                updateDto.setEmail("updated@test.fr");

                mockMvc.perform(put("/api/garages/{id}", savedGarage.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Garage Updated"))
                                .andExpect(jsonPath("$.city").value("Lyon"));
        }

        @Test
        @DisplayName("DELETE /api/garages/{id} - Supprimer un garage")
        void deleteGarage_Success() throws Exception {
                Garage savedGarage = garageRepository.save(
                                new Garage("Garage To Delete", "Delete Addr", "0123456789", "delete@test.fr"));

                mockMvc.perform(delete("/api/garages/{id}", savedGarage.getId()))
                                .andExpect(status().isNoContent());

                mockMvc.perform(get("/api/garages/{id}", savedGarage.getId()))
                                .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("GET /api/garages/search/by-name - Recherche par nom")
        void searchByName_Success() throws Exception {
                garageRepository.save(new Garage("Renault Paris", "Addr", "0123456789", "paris@test.fr"));
                garageRepository.save(new Garage("Renault Lyon", "Addr", "0123456789", "lyon@test.fr"));
                garageRepository.save(new Garage("Peugeot Paris", "Addr", "0123456789", "peugeot@test.fr"));

                mockMvc.perform(get("/api/garages/search/by-name")
                                .param("name", "Renault"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content", hasSize(2)));
        }
}
