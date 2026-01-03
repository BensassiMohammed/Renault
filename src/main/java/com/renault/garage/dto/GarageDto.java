package com.renault.garage.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class GarageDto {

    private Long id;

    @NotBlank(message = "Le nom du garage est obligatoire")
    private String name;

    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotNull(message = "Les horaires d'ouverture sont obligatoires")
    private Map<DayOfWeek, List<OpeningTimeDto>> horairesOuverture;

    private Integer vehicleCount;
}
