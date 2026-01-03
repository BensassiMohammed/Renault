package com.renault.garage.enums;

/**
 * Types d'accessoires disponibles pour les véhicules.
 */
public enum AccessoryType {
    INTERIOR("Intérieur"),
    EXTERIOR("Extérieur"),
    ELECTRONIC("Électronique"),
    SAFETY("Sécurité"),
    COMFORT("Confort"),
    PERFORMANCE("Performance");

    private final String displayName;

    AccessoryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
