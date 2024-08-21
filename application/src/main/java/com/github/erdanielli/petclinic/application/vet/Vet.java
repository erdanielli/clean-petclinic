package com.github.erdanielli.petclinic.application.vet;

import com.github.erdanielli.petclinic.application.domain.PersonName;

import java.util.List;

public record Vet(int id, PersonName name, List<Specialty> specialties) {
    public Vet {
        specialties = List.copyOf(specialties);
    }
}
