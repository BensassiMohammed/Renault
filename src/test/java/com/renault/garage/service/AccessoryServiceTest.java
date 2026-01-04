package com.renault.garage.service;

import com.renault.garage.dto.AccessoryDto;
import com.renault.garage.entity.Accessory;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.AccessoryType;
import com.renault.garage.enums.FuelType;
import com.renault.garage.exception.AccessoryNotFoundException;
import com.renault.garage.exception.VehicleNotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccessoryServiceTest {

    @Mock
    private AccessoryRepository accessoryRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private AccessoryMapper mapper;

    @InjectMocks
    private AccessoryService accessoryService;

    private Vehicle vehicle;
    private Accessory accessory;
    private AccessoryDto accessoryDto;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle("Renault", "Megane", 2023, FuelType.DIESEL);
        vehicle.setId(1L);

        accessory = new Accessory("GPS Navigation", "Système de navigation",
                new BigDecimal("4999.99"), AccessoryType.ELECTRONIC);
        accessory.setId(1L);
        accessory.setVehicle(vehicle);

        accessoryDto = new AccessoryDto();
        accessoryDto.setId(1L);
        accessoryDto.setNom("GPS Navigation");
        accessoryDto.setDescription("Système de navigation");
        accessoryDto.setPrix(new BigDecimal("4999.99"));
        accessoryDto.setType(AccessoryType.ELECTRONIC);
        accessoryDto.setVehicleId(1L);
    }

    @Test
    @DisplayName("Ajouter un accessoire - succès")
    void addAccessoryToVehicle_Success() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(mapper.toEntity(any(AccessoryDto.class))).thenReturn(accessory);
        when(accessoryRepository.save(any(Accessory.class))).thenReturn(accessory);
        when(mapper.toDto(any(Accessory.class))).thenReturn(accessoryDto);

        AccessoryDto result = accessoryService.addAccessoryToVehicle(1L, accessoryDto);

        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("GPS Navigation");
        verify(accessoryRepository).save(any(Accessory.class));
    }

    @Test
    @DisplayName("Ajouter un accessoire - véhicule non trouvé")
    void addAccessoryToVehicle_VehicleNotFound() {
        when(vehicleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accessoryService.addAccessoryToVehicle(99L, accessoryDto))
                .isInstanceOf(VehicleNotFoundException.class);
    }

    @Test
    @DisplayName("Lister les accessoires d'un véhicule - succès")
    void getAccessoriesByVehicle_Success() {
        when(vehicleRepository.existsById(1L)).thenReturn(true);
        when(accessoryRepository.findByVehicleId(1L)).thenReturn(List.of(accessory));
        when(mapper.toDtoList(List.of(accessory))).thenReturn(List.of(accessoryDto));

        List<AccessoryDto> result = accessoryService.getAccessoriesByVehicle(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNom()).isEqualTo("GPS Navigation");
    }

    @Test
    @DisplayName("Lister les accessoires d'un véhicule - véhicule non trouvé")
    void getAccessoriesByVehicle_VehicleNotFound() {
        when(vehicleRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> accessoryService.getAccessoriesByVehicle(99L))
                .isInstanceOf(VehicleNotFoundException.class);
    }

    @Test
    @DisplayName("Mettre à jour un accessoire - succès")
    void updateAccessory_Success() {
        AccessoryDto updateDto = new AccessoryDto();
        updateDto.setNom("GPS Navigation Pro");
        updateDto.setPrix(new BigDecimal("5999.99"));

        when(accessoryRepository.findById(1L)).thenReturn(Optional.of(accessory));
        when(accessoryRepository.save(any(Accessory.class))).thenReturn(accessory);
        when(mapper.toDto(any(Accessory.class))).thenReturn(updateDto);

        AccessoryDto result = accessoryService.updateAccessory(1L, updateDto);

        assertThat(result.getNom()).isEqualTo("GPS Navigation Pro");
        verify(mapper).updateFromDto(updateDto, accessory);
    }

    @Test
    @DisplayName("Mettre à jour un accessoire - non trouvé")
    void updateAccessory_NotFound() {
        when(accessoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accessoryService.updateAccessory(99L, accessoryDto))
                .isInstanceOf(AccessoryNotFoundException.class);
    }

    @Test
    @DisplayName("Supprimer un accessoire - succès")
    void deleteAccessory_Success() {
        when(accessoryRepository.existsById(1L)).thenReturn(true);

        accessoryService.deleteAccessory(1L);

        verify(accessoryRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Supprimer un accessoire - non trouvé")
    void deleteAccessory_NotFound() {
        when(accessoryRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> accessoryService.deleteAccessory(99L))
                .isInstanceOf(AccessoryNotFoundException.class);
    }
}
