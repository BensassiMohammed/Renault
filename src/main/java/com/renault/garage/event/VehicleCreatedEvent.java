package com.renault.garage.event;

import com.renault.garage.enums.FuelType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Événement émis lors de la création d'un nouveau véhicule.
 */
@Getter
@Setter
@ToString
public class VehicleCreatedEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long vehicleId;
    private String brand;
    private String model;
    private Integer manufacturingYear;
    private FuelType fuelType;
    private Long garageId;
    private String garageName;
    private LocalDateTime createdAt;

    public VehicleCreatedEvent() {
        this.createdAt = LocalDateTime.now();
    }

    public VehicleCreatedEvent(Long vehicleId, String brand, String model,
            Integer manufacturingYear, FuelType fuelType,
            Long garageId, String garageName) {
        this.vehicleId = vehicleId;
        this.brand = brand;
        this.model = model;
        this.manufacturingYear = manufacturingYear;
        this.fuelType = fuelType;
        this.garageId = garageId;
        this.garageName = garageName;
        this.createdAt = LocalDateTime.now();
    }
}
