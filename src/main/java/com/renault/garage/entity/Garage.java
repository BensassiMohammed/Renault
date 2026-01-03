package com.renault.garage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Table(name = "garages")
@Getter
@Setter
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du garage est obligatoire")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "L'adresse est obligatoire")
    @Column(nullable = false)
    private String address;

    private String city;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Column(nullable = false)
    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(nullable = false)
    private String email;

    @ElementCollection
    @CollectionTable(name = "garage_opening_hours", joinColumns = @JoinColumn(name = "garage_id"))
    private List<OpeningTime> openingHours = new ArrayList<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();

    public Garage() {
    }

    public Garage(String name, String address, String telephone, String email) {
        this.name = name;
        this.address = address;
        this.telephone = telephone;
        this.email = email;
    }

    // Méthodes spécifiques pour openingHours (groupées par jour)
    public Map<DayOfWeek, List<OpeningTime>> getOpeningHoursMap() {
        return openingHours.stream()
                .collect(Collectors.groupingBy(OpeningTime::getDayOfWeek));
    }

    public void setOpeningHoursMap(Map<DayOfWeek, List<OpeningTime>> openingHoursMap) {
        this.openingHours.clear();
        if (openingHoursMap != null) {
            openingHoursMap.forEach((day, times) -> times.forEach(time -> {
                time.setDayOfWeek(day);
                this.openingHours.add(time);
            }));
        }
    }

    public int getVehicleCount() {
        return vehicles.size();
    }
}
