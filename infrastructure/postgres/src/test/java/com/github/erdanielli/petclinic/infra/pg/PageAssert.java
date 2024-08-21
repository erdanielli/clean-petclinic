package com.github.erdanielli.petclinic.infra.pg;

import org.assertj.core.api.AbstractAssert;
import org.springframework.data.domain.Page;

@SuppressWarnings("UnusedReturnValue")
public class PageAssert<T> extends AbstractAssert<PageAssert<T>, Page<T>> {

    public PageAssert(Page<T> actual) {
        super(actual, PageAssert.class);
    }

    public static <E> PageAssert<E> assertThat(Page<E> actual) {
        return new PageAssert<>(actual);
    }

    public PageAssert<T> isUnpaged() {
        isNotNull();
        if (actual.getPageable().isPaged() || actual.getTotalPages() > 1) {
            failWithMessage("should be unpaged");
        }
        if (actual.getTotalPages() > 1) {
            failWithMessage("should have a single page for unpaged request, got %d", actual.getTotalPages());
        }
        return this;
    }

    public PageAssert<T> hasTotalPages(int totalPages) {
        isNotNull();
        if (actual.getPageable().isUnpaged()) {
            failWithMessage("should be paged");
        }
        if (actual.getTotalPages() != totalPages) {
            failWithMessage("should have %d pages, got %d", totalPages, actual.getTotalPages());
        }
        return this;
    }

    public PageAssert<T> hasTotalElements(int totalElements) {
        isNotNull();
        if (actual.getTotalElements() != totalElements) {
            failWithMessage("should have %d elements, got %d", totalElements, actual.getTotalElements());
        }
        return this;
    }
}
