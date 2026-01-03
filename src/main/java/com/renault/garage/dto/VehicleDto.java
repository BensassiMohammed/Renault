package com.renault.garage.dto;

import com.renault.garage.enums.FuelType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VehicleDto {

    private Long id;

    @NotBlank(message = "La marque est obligatoire")
    private String brand;

    @NotBlank(message = "Le modèle est obligatoire")
    private String model;

    @NotNull(message = "L'année de fabrication est obligatoire")
    @Min(value = 1900, message = "L'année de fabrication doit être supérieure à 1900")
    private Integer anneeFabrication;

    @NotNull(message = "Le type de carburant est obligatoire")
    private FuelType typeCarburant;

    private Long garageId;
    private String garageName;
}
