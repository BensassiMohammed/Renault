package com.renault.garage.service;

import com.renault.garage.dto.VehicleDto;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.event.VehicleEventPublisher;
import com.renault.garage.exception.GarageCapacityExceededException;
import com.renault.garage.exception.GarageNotFoundException;
import com.renault.garage.exception.VehicleNotFoundException;
import com.renault.garage.mapper.VehicleMapper;
import com.renault.garage.repository.GarageRepository;
import com.renault.garage.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final GarageRepository garageRepository;
    private final VehicleMapper mapper;
    private final VehicleEventPublisher vehicleEventPublisher;

    @Value("${app.garage.max-vehicles:50}")
    private int maxVehiclesPerGarage;

    public VehicleDto addVehicleToGarage(Long garageId, VehicleDto vehicleDto) {
        Garage garage = garageRepository.findById(garageId)
                .orElseThrow(() -> new GarageNotFoundException(garageId));

        long currentVehicleCount = vehicleRepository.countByGarageId(garageId);
        if (currentVehicleCount >= maxVehiclesPerGarage) {
            throw new GarageCapacityExceededException(garageId);
        }

        Vehicle vehicle = mapper.toEntity(vehicleDto);
        vehicle.setGarage(garage);
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        vehicleEventPublisher.publishVehicleCreated(savedVehicle);
        return mapper.toDto(savedVehicle);
    }

    @Transactional(readOnly = true)
    public List<VehicleDto> getVehiclesByGarage(Long garageId) {
        if (!garageRepository.existsById(garageId)) {
            throw new GarageNotFoundException(garageId);
        }
        return mapper.toDtoList(vehicleRepository.findByGarageId(garageId));
    }

    @Transactional(readOnly = true)
    public List<VehicleDto> getVehiclesByModel(String model) {
        return mapper.toDtoList(vehicleRepository.findByModel(model));
    }

    public VehicleDto updateVehicle(Long id, VehicleDto vehicleDto) {
        Vehicle existingVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new VehicleNotFoundException(id));
        mapper.updateFromDto(vehicleDto, existingVehicle);
        return mapper.toDto(vehicleRepository.save(existingVehicle));
    }

    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new VehicleNotFoundException(id);
        }
        vehicleRepository.deleteById(id);
    }
}
