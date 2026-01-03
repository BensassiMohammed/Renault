package com.renault.garage.controller;

import com.renault.garage.dto.GarageDto;
import com.renault.garage.enums.FuelType;
import com.renault.garage.service.GarageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/garages")
@RequiredArgsConstructor
@Tag(name = "Garages", description = "API de gestion des garages Renault")
public class GarageController {

    private final GarageService garageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Créer un garage", description = "Crée un nouveau garage. Champs obligatoires: name, address, telephone, email, horairesOuverture")
    public GarageDto createGarage(@Valid @RequestBody GarageDto garageDto) {
        return garageService.createGarage(garageDto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un garage", description = "Récupère un garage par son ID")
    public GarageDto getGarageById(
            @Parameter(description = "ID du garage") @PathVariable Long id) {
        return garageService.getGarageById(id);
    }

    @GetMapping
    @Operation(summary = "Lister tous les garages", description = "Récupère la liste paginée de tous les garages avec possibilité de tri")
    public Page<GarageDto> getAllGarages(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return garageService.getAllGarages(pageable);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un garage", description = "Met à jour les informations d'un garage existant")
    public GarageDto updateGarage(
            @Parameter(description = "ID du garage") @PathVariable Long id,
            @Valid @RequestBody GarageDto garageDto) {
        return garageService.updateGarage(id, garageDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un garage", description = "Supprime un garage du réseau")
    public void deleteGarage(
            @Parameter(description = "ID du garage") @PathVariable Long id) {
        garageService.deleteGarage(id);
    }

    @GetMapping("/search/by-fuel-type")
    @Operation(summary = "Rechercher par type de véhicule", description = "Recherche des garages par type de véhicule pris en charge")
    public List<GarageDto> searchByFuelType(
            @Parameter(description = "Type de carburant (ESSENCE, DIESEL, ELECTRIC, HYBRID)") @RequestParam FuelType typeCarburant) {
        return garageService.getGaragesByVehicleFuelType(typeCarburant);
    }

    @GetMapping("/search/by-accessory")
    @Operation(summary = "Rechercher par accessoire", description = "Recherche des garages contenant un accessoire spécifique dans au moins un véhicule")
    public List<GarageDto> searchByAccessory(
            @Parameter(description = "Nom de l'accessoire") @RequestParam String nom) {
        return garageService.getGaragesByAccessoryName(nom);
    }
}
