package com.renault.garage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application principale du microservice de gestion des garages Renault.
 * 
 * Ce microservice gère:
 * - Les garages affiliés au réseau Renault
 * - Les véhicules stockés dans ces garages (max 50 par garage)
 * - Les accessoires associés aux véhicules
 */
@SpringBootApplication
public class RenaultGarageApplication {

    public static void main(String[] args) {
        SpringApplication.run(RenaultGarageApplication.class, args);
    }
}
