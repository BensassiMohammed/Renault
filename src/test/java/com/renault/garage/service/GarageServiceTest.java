package com.renault.garage.service;

import com.renault.garage.dto.GarageDto;
import com.renault.garage.entity.Garage;
import com.renault.garage.exception.GarageNotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.repository.GarageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GarageServiceTest {

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private GarageMapper mapper;

    @InjectMocks
    private GarageService garageService;

    private Garage garage;
    private GarageDto garageDto;

    @BeforeEach
    void setUp() {
        garage = new Garage("Garage Renault Casablanca", "123 Boulevard Zerktouni", "0522123456", "casa@renault.ma");
        garage.setId(1L);
        garage.setCity("Casablanca");

        garageDto = new GarageDto();
        garageDto.setId(1L);
        garageDto.setName("Garage Renault Casablanca");
        garageDto.setAddress("123 Boulevard Zerktouni");
        garageDto.setCity("Casablanca");
        garageDto.setTelephone("0522123456");
        garageDto.setEmail("casa@renault.ma");
    }

    @Test
    @DisplayName("Créer un garage - succès")
    void createGarage_Success() {
        when(mapper.toEntity(any(GarageDto.class))).thenReturn(garage);
        when(garageRepository.save(any(Garage.class))).thenReturn(garage);
        when(mapper.toDto(any(Garage.class))).thenReturn(garageDto);

        GarageDto result = garageService.createGarage(garageDto);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Garage Renault Casablanca");
        verify(garageRepository).save(any(Garage.class));
    }

    @Test
    @DisplayName("Récupérer un garage par ID - succès")
    void getGarageById_Success() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(mapper.toDto(garage)).thenReturn(garageDto);

        GarageDto result = garageService.getGarageById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Récupérer un garage par ID - non trouvé")
    void getGarageById_NotFound() {
        when(garageRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> garageService.getGarageById(99L)).isInstanceOf(GarageNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Lister tous les garages avec pagination")
    void getAllGarages_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Garage> garagePage = new PageImpl<>(List.of(garage));

        when(garageRepository.findAll(pageable)).thenReturn(garagePage);
        when(mapper.toDto(garage)).thenReturn(garageDto);

        Page<GarageDto> result = garageService.getAllGarages(pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("Mettre à jour un garage - succès")
    void updateGarage_Success() {
        GarageDto updateDto = new GarageDto();
        updateDto.setName("Garage Renault Rabat");

        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(garageRepository.save(any(Garage.class))).thenReturn(garage);
        when(mapper.toDto(any(Garage.class))).thenReturn(updateDto);

        GarageDto result = garageService.updateGarage(1L, updateDto);

        assertThat(result.getName()).isEqualTo("Garage Renault Rabat");
        verify(mapper).updateFromDto(updateDto, garage);
    }

    @Test
    @DisplayName("Supprimer un garage - succès")
    void deleteGarage_Success() {
        when(garageRepository.existsById(1L)).thenReturn(true);
        garageService.deleteGarage(1L);
        verify(garageRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Supprimer un garage - non trouvé")
    void deleteGarage_NotFound() {
        when(garageRepository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> garageService.deleteGarage(99L))
                .isInstanceOf(GarageNotFoundException.class);
    }
}
