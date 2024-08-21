package com.github.erdanielli.petclinic.infra.pg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class JsonRowMapperFactory {
    private final ObjectMapper objectMapper;

    public JsonRowMapperFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> ResultSetExtractor<Page<T>> pageExtractor(Pageable pageable, Class<T> type) {
        return new PageExtractor<>(pageable, type);
    }

    private class PageExtractor<T> implements ResultSetExtractor<Page<T>> {
        final Pageable pageable;
        final Class<T> type;

        PageExtractor(Pageable pageable, Class<T> type) {
            this.pageable = pageable;
            this.type = type;
        }

        @Override
        public Page<T> extractData(ResultSet rs) throws SQLException, DataAccessException {
            if (!rs.next()) return Page.empty(pageable);
            long total = pageable.isUnpaged() ? 0L : rs.getLong(2);
            List<T> content = new ArrayList<>();
            do {
                try (var jsonb = rs.getBinaryStream(1)) {
                    if (jsonb == null) continue;
                    T obj = objectMapper.readValue(jsonb, type);
                    content.add(obj);
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            } while (rs.next());
            return pageable.isUnpaged()
                    ? new PageImpl<>(content)
                    : new PageImpl<>(content, pageable, total);
        }
    }
}
