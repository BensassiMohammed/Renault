package com.renault.garage.exception;

/**
 * Exception levée lorsque la capacité maximale de véhicules d'un garage est
 * atteinte.
 * Chaque garage peut stocker au maximum 50 véhicules.
 */
public class GarageCapacityExceededException extends RuntimeException {

    private static final int MAX_CAPACITY = 50;

    public GarageCapacityExceededException(Long garageId) {
        super("Le garage avec l'id " + garageId + " a atteint sa capacité maximale de " + MAX_CAPACITY + " véhicules");
    }

    public GarageCapacityExceededException(String message) {
        super(message);
    }
}
