package com.github.erdanielli.petclinic.application.vet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UcListVets {

    void execute(Input input, Presenter presenter);

    record Input(Pageable pageable) {
    }

    interface Presenter {
        void showPage(Page<Vet> page);
    }

}
