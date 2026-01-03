package com.renault.garage.controller;

import com.renault.garage.dto.AccessoryDto;
import com.renault.garage.service.AccessoryService;
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
@Tag(name = "Accessoires", description = "API de gestion des accessoires véhicules")
public class AccessoryController {

    private final AccessoryService accessoryService;

    @PostMapping("/vehicles/{vehicleId}/accessories")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ajouter un accessoire", description = "Ajoute un accessoire à un véhicule")
    public AccessoryDto addAccessory(
            @Parameter(description = "ID du véhicule") @PathVariable Long vehicleId,
            @Valid @RequestBody AccessoryDto accessoryDto) {
        return accessoryService.addAccessoryToVehicle(vehicleId, accessoryDto);
    }

    @GetMapping("/vehicles/{vehicleId}/accessories")
    @Operation(summary = "Lister les accessoires d'un véhicule", description = "Récupère tous les accessoires d'un véhicule")
    public List<AccessoryDto> getAccessoriesByVehicle(
            @Parameter(description = "ID du véhicule") @PathVariable Long vehicleId) {
        return accessoryService.getAccessoriesByVehicle(vehicleId);
    }

    @PutMapping("/accessories/{id}")
    @Operation(summary = "Modifier un accessoire", description = "Met à jour les informations d'un accessoire")
    public AccessoryDto updateAccessory(
            @Parameter(description = "ID de l'accessoire") @PathVariable Long id,
            @Valid @RequestBody AccessoryDto accessoryDto) {
        return accessoryService.updateAccessory(id, accessoryDto);
    }

    @DeleteMapping("/accessories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un accessoire", description = "Supprime un accessoire")
    public void deleteAccessory(
            @Parameter(description = "ID de l'accessoire") @PathVariable Long id) {
        accessoryService.deleteAccessory(id);
    }
}
