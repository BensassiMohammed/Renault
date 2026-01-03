package com.renault.garage.exception;

/**
 * Exception levée lorsqu'un garage n'est pas trouvé.
 */
public class GarageNotFoundException extends RuntimeException {

    public GarageNotFoundException(Long id) {
        super("Garage non trouvé avec l'id: " + id);
    }

    public GarageNotFoundException(String message) {
        super(message);
    }
}
