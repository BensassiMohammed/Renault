package com.renault.garage.service;

import com.renault.garage.dto.VehicleDto;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.FuelType;
import com.renault.garage.event.VehicleEventPublisher;
import com.renault.garage.exception.GarageCapacityExceededException;
import com.renault.garage.exception.GarageNotFoundException;
import com.renault.garage.exception.VehicleNotFoundException;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private GarageRepository garageRepository;

    @Mock
    private VehicleMapper mapper;

    @Mock
    private VehicleEventPublisher vehicleEventPublisher;

    @InjectMocks
    private VehicleService vehicleService;

    private Garage garage;
    private Vehicle vehicle;
    private VehicleDto vehicleDto;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(vehicleService, "maxVehiclesPerGarage", 50);

        garage = new Garage("Garage Renault Casablanca", "123 Boulevard Zerktouni",
                "0522123456", "casablanca@renault.ma");
        garage.setId(1L);

        vehicle = new Vehicle("Renault", "Clio", 2023, FuelType.ESSENCE);
        vehicle.setId(1L);
        vehicle.setGarage(garage);

        vehicleDto = new VehicleDto();
        vehicleDto.setId(1L);
        vehicleDto.setBrand("Renault");
        vehicleDto.setModel("Clio");
        vehicleDto.setAnneeFabrication(2023);
        vehicleDto.setTypeCarburant(FuelType.ESSENCE);
        vehicleDto.setGarageId(1L);
    }

    @Test
    @DisplayName("Ajouter un véhicule - succès")
    void addVehicleToGarage_Success() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(vehicleRepository.countByGarageId(1L)).thenReturn(10L);
        when(mapper.toEntity(any(VehicleDto.class))).thenReturn(vehicle);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(mapper.toDto(any(Vehicle.class))).thenReturn(vehicleDto);

        VehicleDto result = vehicleService.addVehicleToGarage(1L, vehicleDto);

        assertThat(result).isNotNull();
        assertThat(result.getBrand()).isEqualTo("Renault");
        assertThat(result.getModel()).isEqualTo("Clio");
        verify(vehicleEventPublisher).publishVehicleCreated(any(Vehicle.class));
    }

    @Test
    @DisplayName("Ajouter un véhicule - capacité dépassée")
    void addVehicleToGarage_CapacityExceeded() {
        when(garageRepository.findById(1L)).thenReturn(Optional.of(garage));
        when(vehicleRepository.countByGarageId(1L)).thenReturn(50L);

        assertThatThrownBy(() -> vehicleService.addVehicleToGarage(1L, vehicleDto))
                .isInstanceOf(GarageCapacityExceededException.class);
    }

    @Test
    @DisplayName("Ajouter un véhicule - garage non trouvé")
    void addVehicleToGarage_GarageNotFound() {
        when(garageRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.addVehicleToGarage(99L, vehicleDto))
                .isInstanceOf(GarageNotFoundException.class);
    }

    @Test
    @DisplayName("Lister les véhicules d'un garage - succès")
    void getVehiclesByGarage_Success() {
        when(garageRepository.existsById(1L)).thenReturn(true);
        when(vehicleRepository.findByGarageId(1L)).thenReturn(List.of(vehicle));
        when(mapper.toDtoList(List.of(vehicle))).thenReturn(List.of(vehicleDto));

        List<VehicleDto> result = vehicleService.getVehiclesByGarage(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBrand()).isEqualTo("Renault");
    }

    @Test
    @DisplayName("Lister les véhicules d'un garage - garage non trouvé")
    void getVehiclesByGarage_GarageNotFound() {
        when(garageRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> vehicleService.getVehiclesByGarage(99L))
                .isInstanceOf(GarageNotFoundException.class);
    }

    @Test
    @DisplayName("Lister les véhicules par modèle - succès")
    void getVehiclesByModel_Success() {
        when(vehicleRepository.findByModel("Clio")).thenReturn(List.of(vehicle));
        when(mapper.toDtoList(List.of(vehicle))).thenReturn(List.of(vehicleDto));

        List<VehicleDto> result = vehicleService.getVehiclesByModel("Clio");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getModel()).isEqualTo("Clio");
    }

    @Test
    @DisplayName("Mettre à jour un véhicule - succès")
    void updateVehicle_Success() {
        VehicleDto updateDto = new VehicleDto();
        updateDto.setBrand("Renault");
        updateDto.setModel("Clio RS");
        updateDto.setAnneeFabrication(2024);
        updateDto.setTypeCarburant(FuelType.ESSENCE);

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(mapper.toDto(any(Vehicle.class))).thenReturn(updateDto);

        VehicleDto result = vehicleService.updateVehicle(1L, updateDto);

        assertThat(result.getModel()).isEqualTo("Clio RS");
        verify(mapper).updateFromDto(updateDto, vehicle);
    }

    @Test
    @DisplayName("Mettre à jour un véhicule - non trouvé")
    void updateVehicle_NotFound() {
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vehicleService.updateVehicle(99L, vehicleDto))
                .isInstanceOf(VehicleNotFoundException.class);
    }

    @Test
    @DisplayName("Supprimer un véhicule - succès")
    void deleteVehicle_Success() {
        when(vehicleRepository.existsById(1L)).thenReturn(true);

        vehicleService.deleteVehicle(1L);

        verify(vehicleRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Supprimer un véhicule - non trouvé")
    void deleteVehicle_NotFound() {
        when(vehicleRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> vehicleService.deleteVehicle(99L))
                .isInstanceOf(VehicleNotFoundException.class);
    }
}
