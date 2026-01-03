package com.renault.garage.service;

import com.renault.garage.dto.AccessoryDto;
import com.renault.garage.entity.Accessory;
import com.renault.garage.entity.Vehicle;
import com.renault.garage.exception.AccessoryNotFoundException;
import com.renault.garage.exception.VehicleNotFoundException;
import com.renault.garage.mapper.AccessoryMapper;
import com.renault.garage.repository.AccessoryRepository;
import com.renault.garage.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccessoryService {

    private final AccessoryRepository accessoryRepository;
    private final VehicleRepository vehicleRepository;
    private final AccessoryMapper mapper;

    public AccessoryDto addAccessoryToVehicle(Long vehicleId, AccessoryDto accessoryDto) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new VehicleNotFoundException(vehicleId));
        Accessory accessory = mapper.toEntity(accessoryDto);
        accessory.setVehicle(vehicle);
        return mapper.toDto(accessoryRepository.save(accessory));
    }

    @Transactional(readOnly = true)
    public List<AccessoryDto> getAccessoriesByVehicle(Long vehicleId) {
        if (!vehicleRepository.existsById(vehicleId)) {
            throw new VehicleNotFoundException(vehicleId);
        }
        return mapper.toDtoList(accessoryRepository.findByVehicleId(vehicleId));
    }

    public AccessoryDto updateAccessory(Long id, AccessoryDto accessoryDto) {
        Accessory existingAccessory = accessoryRepository.findById(id)
                .orElseThrow(() -> new AccessoryNotFoundException(id));
        mapper.updateFromDto(accessoryDto, existingAccessory);
        return mapper.toDto(accessoryRepository.save(existingAccessory));
    }

    public void deleteAccessory(Long id) {
        if (!accessoryRepository.existsById(id)) {
            throw new AccessoryNotFoundException(id);
        }
        accessoryRepository.deleteById(id);
    }
}
