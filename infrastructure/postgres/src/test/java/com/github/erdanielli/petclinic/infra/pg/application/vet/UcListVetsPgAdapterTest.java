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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.NONE;

@SpringBootTest(webEnvironment = NONE)
class UcListVetsPgAdapterTest {
    static UcListVetsPgAdapter useCase;

    @Mock
    UcListVets.Presenter presenter;

    @Captor
    ArgumentCaptor<Page<Vet>> pageCaptor;

    @BeforeEach
    void createUseCaseOnce(@Autowired JdbcClient jdbc, @Autowired JsonRowMapperFactory mapperFactory) {
        if (useCase == null) {
            useCase = new UcListVetsPgAdapter(jdbc, mapperFactory);
        }
    }

    @Test
    void executeUnpaged() {
        var page = shouldShowPageOnce(Pageable.unpaged());
        assertThat(page.getPageable().isUnpaged()).isTrue();
        assertThat(page.getTotalPages()).isOne();
        assertThat(page.getTotalElements()).isEqualTo(6);
    }

    @Test
    void executePaged() {
        var page = shouldShowPageOnce(PageRequest.of(0, 4));
        assertThat(page.getPageable().isUnpaged()).isFalse();
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(6);
    }

    private Page<Vet> shouldShowPageOnce(Pageable pageable) {
        useCase.execute(new UcListVets.Input(pageable), presenter);
        verify(presenter, times(1)).showPage(pageCaptor.capture());
        return pageCaptor.getValue();
    }
}