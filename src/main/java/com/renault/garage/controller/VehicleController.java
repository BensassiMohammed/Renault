package com.renault.garage.controller;

import com.renault.garage.dto.VehicleDto;
import com.renault.garage.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Véhicules", description = "API de gestion des véhicules")
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/garages/{garageId}/vehicles")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter un véhicule", description = "Ajoute un véhicule à un garage (max 50 véhicules par garage)")
    public VehicleDto addVehicle(
            @Parameter(description = "ID du garage") @PathVariable Long garageId,
            @Valid @RequestBody VehicleDto vehicleDto) {
        return vehicleService.addVehicleToGarage(garageId, vehicleDto);
    }

    @GetMapping("/garages/{garageId}/vehicles")
    @Operation(summary = "Lister les véhicules d'un garage", description = "Récupère tous les véhicules d'un garage spécifique")
    public List<VehicleDto> getVehiclesByGarage(
            @Parameter(description = "ID du garage") @PathVariable Long garageId) {
        return vehicleService.getVehiclesByGarage(garageId);
    }

    @GetMapping("/vehicles/model/{model}")
    @Operation(summary = "Lister par modèle", description = "Récupère tous les véhicules d'un modèle donné dans tous les garages")
    public List<VehicleDto> getVehiclesByModel(
            @Parameter(description = "Modèle du véhicule") @PathVariable String model) {
        return vehicleService.getVehiclesByModel(model);
    }

    @PutMapping("/vehicles/{id}")
    @Operation(summary = "Modifier un véhicule", description = "Met à jour les informations d'un véhicule")
    public VehicleDto updateVehicle(
            @Parameter(description = "ID du véhicule") @PathVariable Long id,
            @Valid @RequestBody VehicleDto vehicleDto) {
        return vehicleService.updateVehicle(id, vehicleDto);
    }

    @DeleteMapping("/vehicles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un véhicule", description = "Supprime un véhicule")
    public void deleteVehicle(
            @Parameter(description = "ID du véhicule") @PathVariable Long id) {
        vehicleService.deleteVehicle(id);
    }
}
