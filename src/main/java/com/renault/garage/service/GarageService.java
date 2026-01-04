package com.renault.garage.service;

import com.renault.garage.dto.GarageDto;
import com.renault.garage.entity.Garage;
import com.renault.garage.enums.FuelType;
import com.renault.garage.exception.GarageNotFoundException;
import com.renault.garage.mapper.GarageMapper;
import com.renault.garage.repository.GarageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GarageService {

    private final GarageRepository garageRepository;
    private final GarageMapper mapper;

    public GarageDto createGarage(GarageDto garageDto) {
        Garage garage = mapper.toEntity(garageDto);
        Garage savedGarage = garageRepository.save(garage);
        return mapper.toDto(savedGarage);
    }

    @Transactional(readOnly = true)
    public GarageDto getGarageById(Long id) {
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new GarageNotFoundException(id));
        return mapper.toDto(garage);
    }

    @Transactional(readOnly = true)
    public Page<GarageDto> getAllGarages(Pageable pageable) {
        return garageRepository.findAll(pageable).map(mapper::toDto);
    }

    public GarageDto updateGarage(Long id, GarageDto garageDto) {
        Garage existingGarage = garageRepository.findById(id)
                .orElseThrow(() -> new GarageNotFoundException(id));

        mapper.updateFromDto(garageDto, existingGarage);
        Garage updatedGarage = garageRepository.save(existingGarage);
        return mapper.toDto(updatedGarage);
    }

    public void deleteGarage(Long id) {
        if (!garageRepository.existsById(id)) {
            throw new GarageNotFoundException(id);
        }
        garageRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<GarageDto> getGaragesByVehicleFuelType(FuelType fuelType) {
        List<Garage> garages = garageRepository.findByVehicleFuelType(fuelType);
        return mapper.toDtoList(garages);
    }

    @Transactional(readOnly = true)
    public List<GarageDto> getGaragesByAccessoryName(String accessoryName) {
        List<Garage> garages = garageRepository.findByAccessoryName(accessoryName);
        return mapper.toDtoList(garages);
    }

    @Transactional(readOnly = true)
    public Page<GarageDto> searchGaragesByName(String name, Pageable pageable) {
        return garageRepository.findByNameContainingIgnoreCase(name, pageable).map(mapper::toDto);
    }
}
