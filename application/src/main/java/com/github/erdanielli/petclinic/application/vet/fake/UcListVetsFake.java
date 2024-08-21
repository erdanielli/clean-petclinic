package com.github.erdanielli.petclinic.application.vet.fake;

import com.github.erdanielli.petclinic.application.domain.PersonName;
import com.github.erdanielli.petclinic.application.vet.Specialty;
import com.github.erdanielli.petclinic.application.vet.UcListVets;
import com.github.erdanielli.petclinic.application.vet.Vet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public final class UcListVetsFake implements UcListVets {
    private final List<Vet> fakeList = List.of(
            new Vet(1, new PersonName("James", "Carter"), List.of()),
            new Vet(2, new PersonName("Helen", "Leary"), List.of(new Specialty(1, "radiology"))),
            new Vet(3, new PersonName("Linda", "Douglas"), List.of(new Specialty(2, "surgery"), new Specialty(3, "dentistry"))),
            new Vet(4, new PersonName("Rafael", "Ortega"), List.of(new Specialty(2, "surgery"))),
            new Vet(5, new PersonName("Henry", "Stevens"), List.of(new Specialty(1, "radiology"))),
            new Vet(6, new PersonName("Sharon", "Jenkins"), List.of())
    );

    @Override
    public void execute(Input input, Presenter presenter) {
        var page = extractPage(input.pageable());
        presenter.showPage(page);
    }

    private Page<Vet> extractPage(Pageable pageable) {
        if (pageable.isUnpaged()) return new PageImpl<>(fakeList);
        var slice = fakeList.stream()
                .skip(pageable.getOffset())
                .limit(pageable.getPageSize())
                .toList();
        return new PageImpl<>(slice, pageable, fakeList.size());
    }
}
