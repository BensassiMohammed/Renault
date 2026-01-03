package com.renault.garage.mapper;

import com.renault.garage.dto.GarageDto;
import com.renault.garage.dto.OpeningTimeDto;
import com.renault.garage.entity.Garage;
import com.renault.garage.entity.OpeningTime;
import org.mapstruct.*;

import java.time.DayOfWeek;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GarageMapper {

    @Mapping(target = "horairesOuverture", expression = "java(toOpeningHoursDtoMap(garage.getOpeningHoursMap()))")
    @Mapping(target = "vehicleCount", expression = "java(garage.getVehicleCount())")
    GarageDto toDto(Garage garage);

    @Mapping(target = "openingHours", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    Garage toEntity(GarageDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "openingHours", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    void updateFromDto(GarageDto dto, @MappingTarget Garage garage);

    List<GarageDto> toDtoList(List<Garage> garages);

    @AfterMapping
    default void setOpeningHours(GarageDto dto, @MappingTarget Garage garage) {
        if (dto.getHorairesOuverture() != null) {
            garage.setOpeningHoursMap(toOpeningHoursEntityMap(dto.getHorairesOuverture()));
        }
    }

    default Map<DayOfWeek, List<OpeningTimeDto>> toOpeningHoursDtoMap(Map<DayOfWeek, List<OpeningTime>> openingHours) {
        if (openingHours == null)
            return Collections.emptyMap();
        return openingHours.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey, e -> e.getValue().stream()
                        .map(ot -> new OpeningTimeDto(ot.getStartTime(), ot.getEndTime())).toList()));
    }

    default Map<DayOfWeek, List<OpeningTime>> toOpeningHoursEntityMap(
            Map<DayOfWeek, List<OpeningTimeDto>> openingHours) {
        if (openingHours == null)
            return Collections.emptyMap();
        return openingHours.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey, e -> e.getValue().stream()
                        .map(dto -> new OpeningTime(e.getKey(),
                                dto.getStartTime(), dto.getEndTime()))
                        .toList()));
    }
}
