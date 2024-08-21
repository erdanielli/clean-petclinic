package com.github.erdanielli.petclinic.rest.vet;

import com.github.erdanielli.petclinic.application.vet.UcListVets;
import com.github.erdanielli.petclinic.application.vet.Vet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
@RequestMapping("/api/vets")
public class UcListVetsEndpoint {
    private final UcListVets useCase;

    public UcListVetsEndpoint(UcListVets useCase) {
        this.useCase = useCase;
    }

    @GetMapping
    public PagedModel<Vet> execute(@PageableDefault Pageable pageable) {
        var presenter = new PresenterAdapter();
        useCase.execute(new UcListVets.Input(pageable), presenter);
        return presenter.get();
    }

    private static class PresenterAdapter implements UcListVets.Presenter, Supplier<PagedModel<Vet>> {
        Page<Vet> page;

        @Override
        public void showPage(Page<Vet> page) {
            this.page = page;
        }

        @Override
        public PagedModel<Vet> get() {
            return new PagedModel<>(page);
        }
    }
}
