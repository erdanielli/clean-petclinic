package com.github.erdanielli.petclinic.infra.pg.application.vet;

import com.github.erdanielli.petclinic.application.vet.UcListVets;
import com.github.erdanielli.petclinic.application.vet.Vet;
import com.github.erdanielli.petclinic.infra.pg.JsonRowMapperFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE)
class UcListVetsPgAdapterTest {
    @Autowired
    JdbcClient jdbcClient;
    @Autowired
    JsonRowMapperFactory factory;

    static UcListVetsPgAdapter useCase;

    @Mock
    UcListVets.Presenter presenter;

    @Captor
    ArgumentCaptor<Page<Vet>> pageCaptor;

    @BeforeEach
    void createUseCaseOnce() {
        if (useCase == null) {
            useCase = new UcListVetsPgAdapter(jdbcClient, factory);
        }
    }

    @Test
    void executeUnpaged() {
        useCase.execute(new UcListVets.Input(Pageable.unpaged()), presenter);
        verify(presenter).showPage(pageCaptor.capture());
        var page = pageCaptor.getValue();
        assertThat(page.getPageable().isUnpaged()).isTrue();
        assertThat(page.getTotalPages()).isOne();
        assertThat(page.getTotalElements()).isEqualTo(6);
    }

    @Test
    void executePaged() {
        useCase.execute(new UcListVets.Input(PageRequest.of(0, 4)), presenter);
        verify(presenter).showPage(pageCaptor.capture());
        var page = pageCaptor.getValue();
        assertThat(page.getPageable().isUnpaged()).isFalse();
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(6);
    }
}