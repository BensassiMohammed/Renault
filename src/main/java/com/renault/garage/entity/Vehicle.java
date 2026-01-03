package com.renault.garage.entity;

import com.renault.garage.enums.FuelType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La marque est obligatoire")
    @Column(nullable = false)
    private String brand;

    @NotBlank(message = "Le modèle est obligatoire")
    @Column(nullable = false)
    private String model;

    @NotNull(message = "L'année de fabrication est obligatoire")
    @Min(value = 1900, message = "L'année de fabrication doit être supérieure à 1900")
    @Column(name = "manufacturing_year", nullable = false)
    private Integer manufacturingYear;

    @NotNull(message = "Le type de carburant est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false)
    private FuelType fuelType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id")
    private Garage garage;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Accessory> accessories = new ArrayList<>();

    public Vehicle() {
    }

    public Vehicle(String brand, String model, Integer manufacturingYear, FuelType fuelType) {
        this.brand = brand;
        this.model = model;
        this.manufacturingYear = manufacturingYear;
        this.fuelType = fuelType;
    }

}
