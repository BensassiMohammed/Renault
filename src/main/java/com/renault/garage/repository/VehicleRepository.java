package com.renault.garage.repository;

import com.renault.garage.entity.Vehicle;
import com.renault.garage.enums.FuelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour les opérations sur les véhicules.
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    /**
     * Liste des véhicules d'un garage spécifique.
     */
    List<Vehicle> findByGarageId(Long garageId);

    /**
     * Liste paginée des véhicules d'un garage.
     */
    Page<Vehicle> findByGarageId(Long garageId, Pageable pageable);

    /**
     * Liste des véhicules par modèle (dans tous les garages).
     */
    List<Vehicle> findByModel(String model);

    /**
     * Liste des véhicules par marque.
     */
    List<Vehicle> findByBrand(String brand);

    /**
     * Liste des véhicules par type de carburant.
     */
    List<Vehicle> findByFuelType(FuelType fuelType);

    /**
     * Compte le nombre de véhicules dans un garage.
     */
    @Query("SELECT COUNT(v) FROM Vehicle v WHERE v.garage.id = :garageId")
    long countByGarageId(@Param("garageId") Long garageId);

    /**
     * Liste des véhicules par modèle et marque.
     */
    List<Vehicle> findByBrandAndModel(String brand, String model);
}
