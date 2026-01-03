package com.renault.garage.mapper;

import com.renault.garage.dto.AccessoryDto;
import com.renault.garage.entity.Accessory;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccessoryMapper {

    @Mapping(target = "vehicleId", source = "vehicle.id")
    @Mapping(target = "nom", source = "name")
    @Mapping(target = "prix", source = "price")
    AccessoryDto toDto(Accessory accessory);

    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "name", source = "nom")
    @Mapping(target = "price", source = "prix")
    Accessory toEntity(AccessoryDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicle", ignore = true)
    @Mapping(target = "name", source = "nom")
    @Mapping(target = "price", source = "prix")
    void updateFromDto(AccessoryDto dto, @MappingTarget Accessory accessory);

    List<AccessoryDto> toDtoList(List<Accessory> accessories);
}
