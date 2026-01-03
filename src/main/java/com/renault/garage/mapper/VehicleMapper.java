package com.renault.garage.mapper;

import com.renault.garage.dto.VehicleDto;
import com.renault.garage.entity.Vehicle;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "garageId", source = "garage.id")
    @Mapping(target = "garageName", source = "garage.name")
    @Mapping(target = "anneeFabrication", source = "manufacturingYear")
    @Mapping(target = "typeCarburant", source = "fuelType")
    VehicleDto toDto(Vehicle vehicle);

    @Mapping(target = "garage", ignore = true)
    @Mapping(target = "accessories", ignore = true)
    @Mapping(target = "manufacturingYear", source = "anneeFabrication")
    @Mapping(target = "fuelType", source = "typeCarburant")
    Vehicle toEntity(VehicleDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "garage", ignore = true)
    @Mapping(target = "accessories", ignore = true)
    @Mapping(target = "manufacturingYear", source = "anneeFabrication")
    @Mapping(target = "fuelType", source = "typeCarburant")
    void updateFromDto(VehicleDto dto, @MappingTarget Vehicle vehicle);

    List<VehicleDto> toDtoList(List<Vehicle> vehicles);
}
