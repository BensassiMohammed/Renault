package com.renault.garage.exception;

/**
 * Exception levée lorsqu'un véhicule n'est pas trouvé.
 */
public class VehicleNotFoundException extends RuntimeException {

    public VehicleNotFoundException(Long id) {
        super("Véhicule non trouvé avec l'id: " + id);
    }

    public VehicleNotFoundException(String message) {
        super(message);
    }
}
