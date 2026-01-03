package com.renault.garage.repository;

import com.renault.garage.entity.Garage;
import com.renault.garage.enums.FuelType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour les opérations sur les garages.
 */
@Repository
public interface GarageRepository extends JpaRepository<Garage, Long> {

    /**
     * Recherche paginée de garages par ville.
     */
    Page<Garage> findByCity(String city, Pageable pageable);

    /**
     * Recherche de garages par nom (contient).
     */
    Page<Garage> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Recherche de garages ayant des véhicules d'un type de carburant spécifique.
     */
    @Query("SELECT DISTINCT g FROM Garage g JOIN g.vehicles v WHERE v.fuelType = :fuelType")
    List<Garage> findByVehicleFuelType(@Param("fuelType") FuelType fuelType);

    /**
     * Recherche de garages contenant un accessoire spécifique.
     */
    @Query("SELECT DISTINCT g FROM Garage g JOIN g.vehicles v JOIN v.accessories a WHERE a.name = :accessoryName")
    List<Garage> findByAccessoryName(@Param("accessoryName") String accessoryName);

    /**
     * Recherche de garages par type de véhicule (modèle).
     */
    @Query("SELECT DISTINCT g FROM Garage g JOIN g.vehicles v WHERE v.model = :model")
    List<Garage> findByVehicleModel(@Param("model") String model);
}
