package com.renault.garage.exception;

/**
 * Exception levée lorsqu'un accessoire n'est pas trouvé.
 */
public class AccessoryNotFoundException extends RuntimeException {

    public AccessoryNotFoundException(Long id) {
        super("Accessoire non trouvé avec l'id: " + id);
    }

    public AccessoryNotFoundException(String message) {
        super(message);
    }
}
