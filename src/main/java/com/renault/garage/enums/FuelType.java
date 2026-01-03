package com.renault.garage.enums;

/**
 * Types de carburant supportés pour les véhicules.
 */
public enum FuelType {
    ESSENCE("Essence"),
    DIESEL("Diesel"),
    ELECTRIC("Électrique"),
    HYBRID("Hybride");

    private final String displayName;

    FuelType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
