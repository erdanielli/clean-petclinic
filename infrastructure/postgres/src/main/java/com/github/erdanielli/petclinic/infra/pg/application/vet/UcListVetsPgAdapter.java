package com.github.erdanielli.petclinic.infra.pg.application.vet;

import com.github.erdanielli.petclinic.application.vet.UcListVets;
import com.github.erdanielli.petclinic.application.vet.Vet;
import com.github.erdanielli.petclinic.infra.pg.JsonRowMapperFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.SUPPORTS;

public class UcListVetsPgAdapter implements UcListVets {
    private final JdbcClient jdbc;
    private final JsonRowMapperFactory jsonRowMapperFactory;

    public UcListVetsPgAdapter(JdbcClient jdbc, JsonRowMapperFactory jsonRowMapperFactory) {
        this.jdbc = jdbc;
        this.jsonRowMapperFactory = jsonRowMapperFactory;
    }

    @Override
    @Transactional(readOnly = true, propagation = SUPPORTS)
    public void execute(Input input, Presenter presenter) {
        var pageable = input.pageable();
        Page<Vet> page = pageable.isUnpaged() ? unpagedResult(pageable) : pagedResult(pageable);
        presenter.showPage(page);
    }

    private Page<Vet> unpagedResult(Pageable pageable) {
        var sql = """
                with\
                  q_specialties as (\
                    select\
                      vet_id,\
                      jsonb_agg(jsonb_build_object('id',specialty_id,'name', name) order by name) as specialties\
                    from vet\
                      inner join vet_specialties using (vet_id)\
                      inner join specialty using (specialty_id)\
                    group by vet_id)\
                select\
                  jsonb_build_object(\
                    'id', vet_id,\
                    'name', jsonb_build_object('first', first_name, 'last', last_name),\
                    'specialties', coalesce(specialties, '[]'::jsonb)) as vets \
                from vet left join q_specialties using (vet_id)
                """;
        return jdbc.sql(sql)
                .query(jsonRowMapperFactory.pageExtractor(pageable, Vet.class));

    }

    private Page<Vet> pagedResult(Pageable pageable) {
        var sql = """
                with\
                  q_all as (\
                    select row_number() over (order by vet_id) as id, vet_id\
                    from vet\
                    limit (:offset + :page_size)),\
                  q_vets_page as (\
                    select vet.*\
                    from q_all inner join vet using (vet_id)\
                    where id > :offset\
                    limit :page_size),\
                  q_specialties as (\
                    select\
                      vet_id,\
                      jsonb_agg(jsonb_build_object('id',specialty_id,'name', name) order by name) as specialties\
                    from q_vets_page\
                      inner join vet_specialties using (vet_id)\
                      inner join specialty using (specialty_id)\
                    group by vet_id)\
                select\
                  jsonb_build_object(\
                    'id', vet_id,\
                    'name', jsonb_build_object('first', first_name, 'last', last_name),\
                    'specialties', coalesce(specialties, '[]'::jsonb)) as vets,\
                  (select count(1) from vet) as total \
                from q_vets_page left join q_specialties using (vet_id)
                """;
        return jdbc.sql(sql)
                .param("offset", pageable.getOffset())
                .param("page_size", pageable.getPageSize())
                .query(jsonRowMapperFactory.pageExtractor(pageable, Vet.class));
    }
}
