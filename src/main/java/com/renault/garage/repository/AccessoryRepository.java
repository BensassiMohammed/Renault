package com.renault.garage.repository;

import com.renault.garage.entity.Accessory;
import com.renault.garage.enums.AccessoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour les opérations sur les accessoires.
 */
@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    /**
     * Liste des accessoires d'un véhicule spécifique.
     */
    List<Accessory> findByVehicleId(Long vehicleId);

    /**
     * Liste des accessoires par type.
     */
    List<Accessory> findByType(AccessoryType type);

    /**
     * Liste des accessoires par nom (recherche partielle).
     */
    List<Accessory> findByNameContainingIgnoreCase(String name);
}
